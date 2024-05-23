package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

@Composable
fun FriendLinkItem(logoUrl: String?, name: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        if (TextUtils.isEmpty(logoUrl)) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.thinking_default),
                contentDescription = null
            )
        } else {
            AsyncImage(
                model = logoUrl,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.thinking_default)
            )
        }
        Text(text = name, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}