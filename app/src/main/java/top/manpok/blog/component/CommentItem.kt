package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogComment
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.TempData

@Composable
fun CommentItem(
    data: BlogComment.Data?,
    modifier: Modifier = Modifier,
    onReplyClick: (parentCommentId: String?, replyCommentId: String?, replyUserName: String?) -> Unit
) {
    if (data != null) {
        Row(verticalAlignment = Alignment.Top, modifier = modifier.fillMaxWidth()) {
            AsyncImage(
                model = (if (TempData.currentEnv == Constants.ENV_PROD) Constants.BASE_COMMENT_AVATAR_URL else Constants.BASE_COMMENT_AVATAR_URL_DEV) + data.userAvatar,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                if (data.replyUserName != null && !TextUtils.isEmpty(data.replyUserName)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = data.userName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight(500),
                            modifier = Modifier.padding(end = 5.dp),
                            color = colorResource(R.color.text_article_title)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_reply_to),
                            contentDescription = null,
                            tint = colorResource(id = R.color.text_category_author_name)
                        )
                        Text(
                            text = data.replyUserName ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight(500),
                            modifier = Modifier.padding(start = 5.dp),
                            color = colorResource(R.color.text_article_title)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                } else {
                    Text(
                        text = data.userName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = colorResource(R.color.text_article_title)
                    )
                }
                Text(
                    text = data.content,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = colorResource(R.color.text_article_title)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = data.updateTime ?: "",
                        fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.text_category_author_name
                        ),
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_reply_comment),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onReplyClick(data.parentCommentId, data.id, data.userName)
                        },
                        tint = colorResource(R.color.text_article_title)
                    )
                }
                if (data.children != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        data.children.forEach {
                            CommentItem(
                                data = it,
                                onReplyClick = onReplyClick
                            )
                        }
                    }
                }
            }
        }
    }
}