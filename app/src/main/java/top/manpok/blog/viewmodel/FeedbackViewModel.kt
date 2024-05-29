package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import okhttp3.internal.immutableListOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.FeedbackItemData
import top.manpok.blog.pojo.FeedbackRequest
import top.manpok.blog.utils.Constants

class FeedbackViewModel : ViewModel() {
    private val TAG = "FeedbackViewModel"

    var title = mutableStateOf("")
    var content = mutableStateOf("")
    var email = mutableStateOf("")

    val itemList = immutableListOf(
        FeedbackItemData(
            label = R.string.title,
            hint = R.string.please_enter_feedback_title,
            text = title,
            onTextChange = {
                title.value = it
            }
        ),
        FeedbackItemData(
            label = R.string.feedback_content,
            hint = R.string.please_enter_feedback_content,
            text = content,
            onTextChange = {
                content.value = it
            }
        ),
        FeedbackItemData(
            label = R.string.email,
            hint = R.string.please_enter_feedback_email,
            text = email,
            onTextChange = {
                email.value = it
            }
        )
    )

    fun submitFeedback() {
        BlogRetrofit.feedbackApi.submitFeedback(
            FeedbackRequest(
                content = this.content.value,
                email = this.email.value,
                title = this.title.value
            )
        ).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == Constants.CODE_SUCCESS) {
                        Log.d(TAG, "onResponse: 反馈成功")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }
        })
    }
}