package top.manpok.blog.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ImageApi {

    @GET
    fun getImageStream(@Url url: String): Call<ResponseBody>
}