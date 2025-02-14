package top.manpok.blog.component

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun SettingItem(
    @StringRes name: Int,
    @DrawableRes rightIcon: Int,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    click: (context: Context) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    click(context)
                }
                .padding(0.dp, 15.dp)
        ) {
            Text(
                text = stringResource(id = name),
                color = colorResource(R.color.text_article_title),
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = rightIcon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colorResource(R.color.text_article_title)
            )
        }
        if (!isLast) {
            HorizontalDivider(thickness = 0.5.dp)
        }
    }

}