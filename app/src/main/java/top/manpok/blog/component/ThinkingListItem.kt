package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogThinking
import top.manpok.blog.utils.Constants

@Composable
fun ThinkingListItem(data: BlogThinking.Data?, click: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(5),
        modifier = Modifier.clickable(onClick = click)
    ) {
        Column {
            data?.images?.let {
                val split = it.split("-")
                val cover = split[0]
                if (!TextUtils.isEmpty(cover)) {
                    AsyncImage(
                        model = Constants.BASE_IMAGE_URL + cover,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.thinking_default)
                    )
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.thinking_default),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
            if (data?.images == null) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.thinking_default),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = data?.title!!,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            AuthorInfoBanner(
                avatarUrl = Constants.BASE_IMAGE_URL + data.user?.avatar,
                name = data.user?.userName!!,
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 5.dp)
            )
        }
    }
}