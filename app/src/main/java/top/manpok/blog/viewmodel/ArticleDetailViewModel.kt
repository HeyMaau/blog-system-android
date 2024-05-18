package top.manpok.blog.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticleDetail
import top.manpok.blog.utils.Constants

class ArticleDetailViewModel(val id: String?) : ViewModel() {

    private val TAG = "ArticleDetailViewModel"

    var title by mutableStateOf("")
    var authorAvatar by mutableStateOf("")
    var authorName by mutableStateOf("")
    var authorSign by mutableStateOf("")
    var cover by mutableStateOf("")
    var content by mutableStateOf("")
    var updateTime by mutableStateOf("")

    init {
        getArticleDetail(id)
    }

    fun getArticleDetail(id: String?) {
        if (id != null && !TextUtils.isEmpty(id)) {
            BlogRetrofit.articleApi.getArticleDetail(id)
                .enqueue(object : Callback<BaseResponse<BlogArticleDetail>> {
                    override fun onResponse(
                        call: Call<BaseResponse<BlogArticleDetail>>,
                        response: Response<BaseResponse<BlogArticleDetail>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.code == Constants.CODE_SUCCESS) {
                                val data = body.data
                                title = data?.title!!
                                authorAvatar = Constants.BASE_IMAGE_URL + data.user?.avatar
                                authorName = data.user?.userName!!
                                authorSign = data.user.sign!!
                                cover = Constants.BASE_IMAGE_URL + data.cover!!
                                updateTime = data.updateTime!!
                                setHtmlContent(data.content!!)
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseResponse<BlogArticleDetail>>,
                        error: Throwable
                    ) {
                        Log.d(TAG, "onFailure: $error")
                    }

                })
        }
    }

    private fun setHtmlContent(data: String) {
        content = """
            <!DOCTYPE html>
            <html lang="zh">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no,maximum-scale=1.0,minimum-scale=1.0">
                <title>Article Title</title>
                <link rel="stylesheet" href="highlight.default.min.css"/>
                <link rel="stylesheet" href="global.css"/>
                <link rel="stylesheet" href="article.detail.css"/>
            </head>
            <body>
                <div class="container">
                    $data
                </div>
            </body>
            <script src="highlight.min.js"></script>
            <script>
                function blogHighlightAll() {
                    hljs.highlightAll()
                }
            </script>
            </html>
        """.trimIndent()
    }
}