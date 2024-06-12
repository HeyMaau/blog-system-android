package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogComment

interface CommentApi {

    @POST("/portal/comment")
    fun addComment(@Body blogComment: BlogComment.Data): Call<BaseResponse<Unit>>

    @GET("/portal/comment/list/{type}/{articleID}")
    fun getCommentList(
        @Path("type") type: Int,
        @Path("articleID") articleID: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<BaseResponse<BlogComment>>
}