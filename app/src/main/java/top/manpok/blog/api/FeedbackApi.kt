package top.manpok.blog.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.FeedbackRequest

interface FeedbackApi {

    @POST("/portal/feedback")
    fun submitFeedback(@Body feedbackRequest: FeedbackRequest): Call<BaseResponse<Unit>>
}