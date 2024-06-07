package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.utils.Emoji

@Composable
fun CommentInput(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color = colorResource(id = R.color.gray_f8f8fa), shape = CircleShape)
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.write_comment),
            fontSize = 14.sp,
            color = colorResource(
                id = R.color.gray_878789
            )
        )
        Text(text = "${Emoji.list[3]} ${Emoji.list[10]}", fontSize = 16.sp)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCommentInput() {
    CommentInput()
}