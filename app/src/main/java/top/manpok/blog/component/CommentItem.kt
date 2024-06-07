package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogComment
import top.manpok.blog.utils.Constants

@Composable
fun CommentItem(data: BlogComment.Data?, modifier: Modifier = Modifier) {
    if (data != null) {
        Row(verticalAlignment = Alignment.Top, modifier = modifier.fillMaxWidth()) {
            AsyncImage(
                model = Constants.BASE_COMMENT_AVATAR_URL + data.userAvatar,
                contentDescription = null,
                modifier = modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = data.userName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = data.content,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = data.updateTime,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_878789)
                )
            }
        }
    }
}