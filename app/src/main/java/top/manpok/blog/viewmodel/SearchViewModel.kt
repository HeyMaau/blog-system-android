package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
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

    fun doSearch() {
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
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<BlogSearchResult>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }
        })
    }
}