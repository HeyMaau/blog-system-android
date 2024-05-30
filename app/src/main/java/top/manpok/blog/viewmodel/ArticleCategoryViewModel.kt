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
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.ToastUtil

class ArticleCategoryViewModel : ViewModel() {
    private val TAG = "ArticleCategoryViewModel"

    val articleList = mutableStateListOf<BlogArticle.Data?>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(5)
    var total by mutableIntStateOf(0)
    var preventGet = false

    var refreshing by mutableStateOf(false)

    fun getArticleListByCategory(page: Int, size: Int, categoryID: String) {
        if (preventGet) return
        BlogRetrofit.articleApi.getArticleListByCategory(page, size, categoryID)
            .enqueue(object : Callback<BaseResponse<BlogArticle>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogArticle>>,
                    response: Response<BaseResponse<BlogArticle>>
                ) {
                    if (refreshing) {
                        ToastUtil.showShortToast(R.string.refresh_successfully)
                    }
                    refreshing = false
                    if (response.isSuccessful) {
                        if (response.body()?.code == Constants.CODE_SUCCESS) {
                            val blogArticle = response.body()?.data
                            currentPage = blogArticle?.currentPage!!
                            noMore = blogArticle.noMore
                            pageSize = blogArticle.pageSize
                            total = blogArticle.total
                            articleList.clear()
                            blogArticle.data?.let { articleList.addAll(it) }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BlogArticle>>, error: Throwable) {
                    if (refreshing) {
                        ToastUtil.showShortToast(R.string.refresh_fail)
                    }
                    refreshing = false
                    Log.d(TAG, "onFailure: $error")
                }

            })
        preventGet = true
    }
}