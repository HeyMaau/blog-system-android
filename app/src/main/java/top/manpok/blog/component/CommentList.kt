package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.manpok.blog.pojo.BlogComment

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    dataList: List<BlogComment.Data?>,
    onReplyClick: (parentCommentId: String?, replyCommentId: String?, replyUserName: String?) -> Unit
) {
    Column(modifier = modifier) {
        dataList.forEach {
            CommentItem(data = it, onReplyClick = onReplyClick)
        }
    }
}