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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.manpok.blog.R
import top.manpok.blog.pojo.FeedbackItemData

@Composable
fun FeedbackInputArea(dataList: List<FeedbackItemData>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10))
            .background(Color.White)
            .padding(top = 0.dp, start = 12.dp, end = 12.dp, bottom = 20.dp)
    ) {
        dataList.forEach { data ->
            TextField(
                value = data.text.value,
                onValueChange = data.onTextChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedPlaceholderColor = colorResource(id = R.color.gray_878789),
                    focusedLabelColor = colorResource(id = R.color.blue_4285f4),
                    focusedIndicatorColor = colorResource(id = R.color.blue_4285f4),
                    unfocusedIndicatorColor = colorResource(id = R.color.gray_878789)
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