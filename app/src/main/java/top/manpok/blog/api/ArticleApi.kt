package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.pojo.BlogArticleDetail

interface ArticleApi {

    @GET("/portal/article/list")
    fun getArticleList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<BaseResponse<BlogArticle>>

    @GET("/portal/article/{id}")
    fun getArticleDetail(
        @Path("id") id: String
    ): Call<BaseResponse<BlogArticleDetail>>

    @GET("/portal/article/list")
    fun getArticleListByCategory(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categoryID") categoryID: String
    ): Call<BaseResponse<BlogArticle>>
}