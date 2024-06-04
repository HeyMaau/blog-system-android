package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.db.SearchDatabase
import top.manpok.blog.db.entity.BlogSearchHistory
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogSearchResult
import top.manpok.blog.utils.Constants

class SearchViewModel : ViewModel() {
    private val TAG = "SearchViewModel"

    var keywords by mutableStateOf("")
    var searchList = mutableStateListOf<BlogSearchResult.Data?>()
    var currentPage by mutableIntStateOf(Constants.DEFAULT_PAGE)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(Constants.DEFAULT_PAGE_SIZE)
    var total by mutableIntStateOf(0)

    var isLoading by mutableStateOf(false)
    var beginSearch by mutableStateOf(false)
    var loadingTimeOut = false

    var searchHistoryList = mutableStateListOf<BlogSearchHistory>()

    var commonHeaderHeight by mutableStateOf(0.dp)

    init {
        viewModelScope.launch {
            loadDB()
        }
    }

    fun doSearch() {

        loadingTimeOut = false
        isLoading = true

        viewModelScope.launch {
            delay(500L)
            if (searchList.isNotEmpty() || (searchList.isEmpty() && noMore)) {
                isLoading = false
            }
            loadingTimeOut = true
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                saveDB(keyword = keywords)
            }
        }

        BlogRetrofit.searchApi.getArticleList(currentPage, pageSize, keywords).enqueue(object :
            Callback<BaseResponse<BlogSearchResult>> {
            override fun onResponse(
                call: Call<BaseResponse<BlogSearchResult>>,
                response: Response<BaseResponse<BlogSearchResult>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == Constants.CODE_SUCCESS) {
                        val data = body.data
                        currentPage = data?.currentPage!!
                        noMore = data.noMore
                        pageSize = data.pageSize
                        total = data.total
                        data.data?.let {
                            searchList.clear()
                            searchList.addAll(it)
                            if (loadingTimeOut) {
                                isLoading = false
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<BlogSearchResult>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }
        })
    }

    private suspend fun saveDB(keyword: String) {
        val searchHistoryDao =
            SearchDatabase.getDatabase(BaseApplication.getApplication()).searchHistoryDao()
        var searchHistory = searchHistoryDao.getByKeyword(keyword)
        if (searchHistory == null) {
            searchHistory = BlogSearchHistory(
                keyword = keyword,
                count = 1,
                updateTime = System.currentTimeMillis()
            )
            searchHistoryDao.insert(searchHistory)
        } else {
            searchHistory.count++
            searchHistory.updateTime = System.currentTimeMillis()
            searchHistoryDao.update(searchHistory)
        }
    }

    private suspend fun loadDB() {
        val searchHistoryDao =
            SearchDatabase.getDatabase(BaseApplication.getApplication()).searchHistoryDao()
        val searchHistoryListFromDB = searchHistoryDao.getAll()
        searchHistoryListFromDB?.let {
            if (searchHistoryListFromDB.isNotEmpty()) {
                searchHistoryList.addAll(searchHistoryListFromDB)
            }
        }
    }
}