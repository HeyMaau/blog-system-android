package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
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
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.ToastUtil

class ThinkingViewModel : ViewModel() {

    private val TAG = "ThinkingViewModel"

    private val mutex = Mutex()

    val thinkingList = mutableStateListOf<BlogThinking.Data>()
    var currentPage by mutableIntStateOf(Constants.DEFAULT_PAGE)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(Constants.DEFAULT_PAGE_SIZE)
    var total by mutableIntStateOf(0)

    var refreshing by mutableStateOf(false)
    var loading by mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
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
                            blogThinking.data?.let {
                                if (currentPage == Constants.DEFAULT_PAGE) {
                                    thinkingList.clear()
                                    viewModelScope.launch(Dispatchers.IO) {
                                        mutex.withLock {
                                            saveToDB(blogThinking.data)
                                        }
                                    }
                                }
                                thinkingList.addAll(it)
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
                    LogUtil.d(TAG, "onFailure: $error")
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

    private suspend fun convertDBData(data: List<BlogThinkingListItemForDB?>?) {
        val tempDataList = mutableListOf<BlogThinking.Data>()
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
            tempDataList.add(item)
        }
        withContext(Dispatchers.Main) {
            thinkingList.addAll(tempDataList)
        }
    }
}