package top.manpok.blog.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticleDetail

class ArticleDetailViewModel : ViewModel() {

    val title = mutableStateOf("")

    fun getArticleDetail(id: String) {
        BlogRetrofit.articleApi.getArticleDetail(id)
            .enqueue(object : Callback<BaseResponse<BlogArticleDetail>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogArticleDetail>>,
                    response: Response<BaseResponse<BlogArticleDetail>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(
                    call: Call<BaseResponse<BlogArticleDetail>>,
                    error: Throwable
                ) {
                    TODO("Not yet implemented")
                }

            })
    }
}