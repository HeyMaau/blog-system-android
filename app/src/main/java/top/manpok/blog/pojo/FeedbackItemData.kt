package top.manpok.blog.pojo

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState

data class FeedbackItemData(
    @StringRes val label: Int,
    @StringRes val hint: Int,
    var text: MutableState<String>,
    val onTextChange: (String) -> Unit
)
