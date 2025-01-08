package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.pojo.FeedbackItemData

@Composable
fun FeedbackInputArea(dataList: List<FeedbackItemData>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(colorResource(R.color.bg_white))
            .padding(top = 0.dp, start = 12.dp, end = 12.dp, bottom = 20.dp)
    ) {
        dataList.forEach { data ->
            TextField(
                value = data.text.value,
                onValueChange = data.onTextChange,
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.bg_white),
                    unfocusedContainerColor = colorResource(R.color.bg_white),
                    focusedPlaceholderColor = colorResource(id = R.color.text_category_author_name),
                    focusedLabelColor = colorResource(id = R.color.blue_4285f4),
                    unfocusedLabelColor = colorResource(R.color.text_article_title),
                    focusedIndicatorColor = colorResource(id = R.color.blue_4285f4),
                    unfocusedIndicatorColor = colorResource(id = R.color.text_category_author_name),
                    focusedTextColor = colorResource(R.color.text_article_title),
                    unfocusedTextColor = colorResource(R.color.text_category_author_name)
                ),
                label = {
                    Text(
                        text = stringResource(data.label),
                        fontWeight = FontWeight.Bold
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = data.hint)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = if (stringResource(id = R.string.feedback_content).equals(
                                stringResource(id = data.label)
                            )
                        ) 100.dp else TextFieldDefaults.MinHeight
                    )
            )
        }

    }
}