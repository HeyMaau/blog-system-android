package top.manpok.blog.component

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
        textStyle = TextStyle.Default.copy(color = colorResource(R.color.text_article_title)),
        cursorBrush = SolidColor(colorResource(R.color.text_article_title)),
        onValueChange = {
            onTextChange(it)
        },
        decorationBox = { innerTextField ->
            Box {
                if (TextUtils.isEmpty(text.text)) {
                    Text(
                        text = stringResource(id = hintText),
                        color = colorResource(
                            id = R.color.text_category_author_name
                        )
                    )
                }
                innerTextField()
            }
        })
}

@Composable
fun CommentBasicTextField(
    modifier: Modifier = Modifier,
    minLines: Int,
    text: TextFieldValue,
    hintText: String,
    onTextChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        minLines = minLines,
        value = text,
        textStyle = TextStyle.Default.copy(color = colorResource(R.color.text_article_title)),
        cursorBrush = SolidColor(colorResource(R.color.text_article_title)),
        onValueChange = {
            onTextChange(it)
        },
        decorationBox = { innerTextField ->
            Box {
                if (TextUtils.isEmpty(text.text)) {
                    Text(
                        text = hintText,
                        color = colorResource(
                            id = R.color.text_category_author_name
                        )
                    )
                }
                innerTextField()
            }
        })
}