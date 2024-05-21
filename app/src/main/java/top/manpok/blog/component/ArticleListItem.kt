package top.manpok.blog.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.utils.Constants

@Composable
fun ArticleListItem(
    item: BlogArticle.Data?,
    isLast: Boolean,
    click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.clickable(onClick = click)) {
        Text(text = "${item?.title}", fontWeight = FontWeight(500), fontSize = 18.sp)
        AuthorInfoBanner(
            avatarUrl = Constants.BASE_IMAGE_URL + item?.user?.avatar,
            name = "${item?.user?.userName}",
            modifier = Modifier.padding(5.dp),
            fontSize = 16.sp,
            fontColor = R.color.gray_585858
        )
        Text(
            text = "${item?.content}",
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            color = colorResource(
                id = R.color.gray_585858
            )
        )
        if (!isLast) {
            HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(0.dp, 15.dp)
            )
        }
    }
}