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
                                content = data.content!!
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
}