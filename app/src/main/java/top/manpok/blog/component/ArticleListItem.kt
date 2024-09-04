package top.manpok.blog.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import top.manpok.blog.utils.TempData

@Composable
fun ArticleListItem(
    item: BlogArticle.Data?,
    isLast: Boolean,
    click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier.clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                }, indication = null, onClick = click
            )
        ) {
            Text(text = "${item?.title}", fontWeight = FontWeight(500), fontSize = 18.sp)
            AuthorInfoBanner(
                avatarUrl = (if (TempData.currentEnv == Constants.ENV_PROD) Constants.BASE_IMAGE_URL else Constants.BASE_IMAGE_URL_DEV) + item?.user?.avatar,
                name = "${item?.user?.userName}",
                modifier = Modifier.padding(6.dp),
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
            if (item?.cover != null) {
                Spacer(modifier = Modifier.height(6.dp))
                AsyncImage(
                    model = (if (TempData.currentEnv == Constants.ENV_PROD) Constants.BASE_IMAGE_URL else Constants.BASE_IMAGE_URL_DEV) + item.cover,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        if (!isLast) {
            HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(0.dp, 15.dp)
            )
        }
    }
}