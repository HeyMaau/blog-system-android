package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

@Composable
fun FriendLinkItem(
    showSkeleton: Boolean,
    logoUrl: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    if (!showSkeleton) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
            if (TextUtils.isEmpty(logoUrl)) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.thinking_default),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.thinking_default),
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = name,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
            Image(
                painter = ColorPainter(colorResource(id = R.color.gray_cccccc)),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Surface(
                color = colorResource(id = R.color.gray_cccccc),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 10.dp)
                    .height(10.dp)
            ) {

            }
        }
    }
}