package top.manpok.blog.component

import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@Composable
fun CommonHeader(
    title: String?,
    @DrawableRes leftIcon: Int?,
    @DrawableRes rightIcon: Int?,
    leftIconClick: () -> Unit,
    rightIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (leftIcon != null) {
            IconButton(onClick = leftIconClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leftIcon),
                    contentDescription = null
                )
            }
        }
        if (!TextUtils.isEmpty(title)) {
            Text(text = "$title", fontSize = 18.sp)
        }
        if (rightIcon != null) {
            IconButton(onClick = rightIconClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = rightIcon),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCommonHeader() {
    CommonHeader(
        title = "测试标题",
        leftIcon = R.drawable.ic_arrow_back,
        rightIcon = R.drawable.ic_more,
        leftIconClick = { /*TODO*/ },
        rightIconClick = { /*TODO*/ })
}