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
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.utils.Constants

class ArticleViewModel : ViewModel() {

    private val TAG = "ArticleViewModel"

    var articleList = mutableStateListOf<BlogArticle.Data?>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(5)
    var total by mutableIntStateOf(0)

    init {
        getArticleList(currentPage, pageSize)
    }

    fun getArticleList(page: Int, size: Int) {
        BlogRetrofit.articleApi.getArticleList(page, size).enqueue(object :
            Callback<BaseResponse<BlogArticle>> {
            override fun onResponse(
                call: Call<BaseResponse<BlogArticle>>,
                response: Response<BaseResponse<BlogArticle>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.code == Constants.CODE_SUCCESS) {
                        val blogArticle = response.body()?.data
                        currentPage = blogArticle?.currentPage!!
                        noMore = blogArticle.noMore!!
                        pageSize = blogArticle.pageSize!!
                        total = blogArticle.total!!
                        articleList.addAll(blogArticle.data!!)
                    }
                }
            }

            override fun onFailure(response: Call<BaseResponse<BlogArticle>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }

        })
    }
}