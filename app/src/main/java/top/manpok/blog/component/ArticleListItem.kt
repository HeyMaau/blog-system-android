package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
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
import top.manpok.blog.pojo.BlogArticle
import top.manpok.blog.utils.Constants

@Composable
fun ArticleListItem(item: BlogArticle.Data?, isLast: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "${item?.title}", fontWeight = FontWeight(500), fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
            AsyncImage(
                model = Constants.BASE_IMAGE_URL + item?.user?.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "${item?.user?.userName}",
                color = colorResource(id = R.color.gray_585858),
                modifier = Modifier.padding(5.dp, 0.dp)
            )
        }
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