package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.FeedbackItemData
import top.manpok.blog.pojo.FeedbackRequest
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.ToastUtil

class FeedbackViewModel : ViewModel() {
    private val TAG = "FeedbackViewModel"

    var title = mutableStateOf("")
    var content = mutableStateOf("")
    var email = mutableStateOf("")
    var showSubmitProgress by mutableStateOf(false)

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Stop)
    val submitState = _submitState.asStateFlow()

    val itemList = listOf(
        FeedbackItemData(
            label = R.string.feedback_title,
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
            label = R.string.feedback_email,
            hint = R.string.please_enter_feedback_email,
            text = email,
            onTextChange = {
                email.value = it
            }
        )
    )

    fun submitFeedback() {
        showSubmitProgress = true
        _submitState.value = SubmitState.Submitting
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
                        ToastUtil.showShortToast(R.string.feedback_submit_successfully)
                        showSubmitProgress = false
                        _submitState.value = SubmitState.Finish
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, error: Throwable) {
                ToastUtil.showShortToast(R.string.feedback_submit_fail)
                showSubmitProgress = false
                _submitState.value = SubmitState.Error
                LogUtil.e(TAG, "onFailure: $error")
            }
        })
    }

    sealed class SubmitState {
        data object Stop : SubmitState()
        data object Submitting : SubmitState()
        data object Finish : SubmitState()
        data object Error : SubmitState()
    }
}