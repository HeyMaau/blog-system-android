package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAppInfo

interface UpdateApi {

    @GET("/portal/app/checkUpdateInfo")
    fun checkUpdateInfo(
        @Query("version_code") versionCode: Int,
    ): Call<BaseResponse<BlogAppInfo>>
}