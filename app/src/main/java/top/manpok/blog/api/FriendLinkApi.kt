package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogFriendLink

interface FriendLinkApi {

    @GET("/portal/friend_link/list")
    fun getFriendLinkList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<BaseResponse<BlogFriendLink>>
}