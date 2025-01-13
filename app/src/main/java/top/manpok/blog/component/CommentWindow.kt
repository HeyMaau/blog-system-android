package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogComment

@Composable
fun CommentWindow(
    modifier: Modifier = Modifier,
    commentTotal: Int,
    commentList: List<BlogComment.Data?>,
    onReplyClick: (parentCommentId: String?, replyCommentId: String?, replyUserName: String?) -> Unit,
    onInputClick: () -> Unit
) {
    Column {
        if (commentTotal > 0) {
            Text(
                text = stringResource(
                    id = R.string.comment_number,
                    commentTotal
                ),
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                color = colorResource(R.color.text_article_title)
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        CommentInput {
            onInputClick()
        }
        CommentList(
            dataList = commentList,
            onReplyClick = onReplyClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 30.dp)
        )
    }
}