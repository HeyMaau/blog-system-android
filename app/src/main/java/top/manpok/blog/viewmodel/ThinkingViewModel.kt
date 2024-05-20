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
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.db.ThinkingListDatabase
import top.manpok.blog.db.entity.BlogThinkingListItemForDB
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogThinking
import top.manpok.blog.utils.Constants

class ThinkingViewModel : ViewModel() {

    private val TAG = "ThinkingViewModel"

    val thinkingList = mutableStateListOf<BlogThinking.Data>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(5)
    var total by mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            getDataFromDB()
        }
        getThinkingList(currentPage, pageSize)
    }

    private fun getThinkingList(page: Int, size: Int) {
        BlogRetrofit.thinkingApi.getThinkingList(page, size)
            .enqueue(object : Callback<BaseResponse<BlogThinking>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogThinking>>,
                    response: Response<BaseResponse<BlogThinking>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.code == Constants.CODE_SUCCESS) {
                            val blogThinking = response.body()?.data
                            currentPage = blogThinking?.currentPage!!
                            noMore = blogThinking.noMore
                            pageSize = blogThinking.pageSize
                            total = blogThinking.total
                            thinkingList.clear()
                            blogThinking.data?.let { thinkingList.addAll(it) }
                            viewModelScope.launch {
                                saveToDB(blogThinking.data)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BlogThinking>>, error: Throwable) {
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