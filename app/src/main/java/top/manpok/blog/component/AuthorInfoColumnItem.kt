package top.manpok.blog.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun AuthorInfoColumnItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    @DrawableRes rightIcon: Int?,
    click: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = click != null, onClick = {
                click?.invoke()
            })
            .padding(0.dp, 10.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            color = colorResource(id = R.color.gray_878789),
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )
        if (rightIcon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = rightIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}