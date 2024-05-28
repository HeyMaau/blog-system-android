package top.manpok.blog.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import okhttp3.internal.immutableListOf
import top.manpok.blog.R
import top.manpok.blog.pojo.FeedbackItemData

class FeedbackViewModel : ViewModel() {


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

}