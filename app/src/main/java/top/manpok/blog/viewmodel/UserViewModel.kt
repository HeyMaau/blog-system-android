package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogUser
import top.manpok.blog.utils.Constants

class UserViewModel : ViewModel() {

    init {
        getUserInfo()
    }

    var authorName by mutableStateOf("")
    var authorSign by mutableStateOf("")
    var authorAvatar by mutableStateOf("")

    fun getUserInfo() {
        BlogRetrofit.userApi.getUserInfo().enqueue(object : Callback<BaseResponse<BlogUser>> {
            override fun onResponse(
                call: Call<BaseResponse<BlogUser>>,
                response: Response<BaseResponse<BlogUser>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == Constants.CODE_SUCCESS) {
                        val data = body.data
                        authorName = data?.userName!!
                        authorSign = data.sign!!
                        authorAvatar = Constants.BASE_IMAGE_URL + data.avatar
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<BlogUser>>, error: Throwable) {

            }
        })
    }
}