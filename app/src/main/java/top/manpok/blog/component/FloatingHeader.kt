package top.manpok.blog.component

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun FloatingHeader(
    @DrawableRes leftIcon: Int,
    @DrawableRes rightIcon: Int?,
    leftIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    onShareClick: (() -> Unit)? = null
) {
    var showBottomDialog by remember {
        mutableStateOf(false)
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = ImageVector.vectorResource(id = leftIcon),
            contentDescription = null,
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = CircleShape)
                .clip(
                    CircleShape
                )
                .background(color = colorResource(id = R.color.white_f2ffffff))
                .padding(4.dp)
                .clickable(
                    onClick = leftIconClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
        )
        if (rightIcon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = rightIcon),
                contentDescription = null,
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = CircleShape)
                    .clip(
                        CircleShape
                    )
                    .background(color = colorResource(id = R.color.white_f2ffffff))
                    .padding(4.dp)
                    .clickable(onClick = {
                        showBottomDialog = true
                    },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() })
            )
        }
    }
    if (showBottomDialog) {
        ShareBottomDialog(onDismiss = { showBottomDialog = false }, onItemClick = {
            onShareClick?.invoke()
        })
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreviewFloatingHeader() {
    FloatingHeader(
        leftIcon = R.drawable.ic_arrow_back,
        rightIcon = R.drawable.ic_more,
        leftIconClick = {},
        onShareClick = {}
    )
}