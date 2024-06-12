package top.manpok.blog.component

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import top.manpok.blog.R

@Composable
fun CommentBasicTextField(
    modifier: Modifier = Modifier,
    minLines: Int,
    text: TextFieldValue,
    @StringRes hintText: Int,
    onTextChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        minLines = minLines,
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        decorationBox = { innerTextField ->
            Box {
                if (TextUtils.isEmpty(text.text)) {
                    Text(
                        text = stringResource(id = hintText),
                        color = colorResource(
                            id = R.color.gray_878789
                        )
                    )
                }
                innerTextField()
            }
        })
}