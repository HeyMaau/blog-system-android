package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.db.ThinkingListDatabase
import top.manpok.blog.db.entity.BlogThinkingListItemForDB
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogThinking
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.ToastUtil

class ThinkingViewModel : ViewModel() {

    private val TAG = "ThinkingViewModel"

    val thinkingList = mutableStateListOf<BlogThinking.Data>()
    var currentPage by mutableIntStateOf(Constants.DEFAULT_PAGE)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(Constants.DEFAULT_PAGE_SIZE)
    var total by mutableIntStateOf(0)

    var refreshing by mutableStateOf(false)
    var loading by mutableStateOf(false)

    init {
        viewModelScope.launch {
            getDataFromDB()
        }
        getThinkingList(currentPage, pageSize)
    }

    fun getThinkingList(page: Int, size: Int) {
        loading = true
        BlogRetrofit.thinkingApi.getThinkingList(page, size)
            .enqueue(object : Callback<BaseResponse<BlogThinking>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogThinking>>,
                    response: Response<BaseResponse<BlogThinking>>
                ) {
                    if (refreshing) {
                        ToastUtil.showShortToast(R.string.refresh_successfully)
                    }
                    loading = false
                    refreshing = false
                    if (response.isSuccessful) {
                        if (response.body()?.code == Constants.CODE_SUCCESS) {
                            val blogThinking = response.body()?.data
                            currentPage = blogThinking?.currentPage!!
                            noMore = blogThinking.noMore
                            pageSize = blogThinking.pageSize
                            total = blogThinking.total
                            if (currentPage == Constants.DEFAULT_PAGE) {
                                thinkingList.clear()
                            }
                            blogThinking.data?.let { thinkingList.addAll(it) }
                            viewModelScope.launch {
                                saveToDB(blogThinking.data)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BlogThinking>>, error: Throwable) {
                    if (refreshing) {
                        ToastUtil.showShortToast(R.string.refresh_fail)
                    }
                    loading = false
                    refreshing = false
                    Log.d(TAG, "onFailure: $error")
                }

            })
    }

    private suspend fun getDataFromDB() {
        val dataList =
            ThinkingListDatabase.getDatabase(BaseApplication.getApplication()).thinkingListDao()
                .getAll()
        convertDBData(dataList)
    }

    private suspend fun saveToDB(data: List<BlogThinking.Data?>?) {
        val saveList = mutableListOf<BlogThinkingListItemForDB>()
        data?.forEach {
            val item = BlogThinkingListItemForDB(
                id = it?.id!!,
                title = it.title,
                avatar = it.user?.avatar,
                userName = it.user?.userName,
                content = it.content,
                images = it.images,
                sign = it.user?.sign,
                updateTime = it.updateTime
            )
            saveList.add(item)
        }
        val thinkingListDao =
            ThinkingListDatabase.getDatabase(BaseApplication.getApplication()).thinkingListDao()
        thinkingListDao.deleteAll()
        thinkingListDao.insertAll(saveList)
    }

    private fun convertDBData(data: List<BlogThinkingListItemForDB?>?) {
        data?.forEach {
            val item = BlogThinking.Data(
                id = it?.id,
                title = it?.title,
                user = BlogThinking.User(
                    avatar = it?.avatar,
                    userName = it?.userName,
                    sign = it?.sign
                ),
                content = it?.content,
                images = it?.images,
                updateTime = it?.updateTime
            )
            thinkingList.add(item)
        }
    }
}