package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.GET
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogUser

interface UserApi {

    @GET("/portal/user/admin")
    fun getUserInfo(): Call<BaseResponse<BlogUser>>
}