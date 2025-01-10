package top.manpok.blog.component

import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@Composable
fun CommonHeader(
    title: String?,
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int?,
    @DrawableRes rightIcon: Int?,
    @DrawableRes closeIcon: Int? = null,
    leftIconClick: () -> Unit,
    closeIconClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    var showBottomDialog by remember {
        mutableStateOf(false)
    }
    Box(contentAlignment = Alignment.CenterStart, modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leftIcon != null) {
                Box(
                    modifier = Modifier
                        .padding(0.dp, 6.dp, 12.dp, 6.dp)
                        .clickable(onClick = leftIconClick)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = leftIcon),
                        contentDescription = null,
                        tint = colorResource(R.color.text_article_title)
                    )
                }
            }
            if (closeIcon != null) {
                Box(
                    modifier = Modifier
                        .padding(2.dp, 6.dp, 12.dp, 6.dp)
                        .clickable(onClick = {
                            closeIconClick?.invoke()
                        })
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = closeIcon),
                        contentDescription = null,
                        tint = colorResource(R.color.text_article_title)
                    )
                }
            }
        }
        if (!TextUtils.isEmpty(title)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "$title",
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    color = colorResource(R.color.text_article_title)
                )
            }
        }
        if (rightIcon != null) {
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(12.dp, 6.dp, 0.dp, 6.dp)
                        .clickable(onClick = {
                            showBottomDialog = true
                        })
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = rightIcon),
                        contentDescription = null,
                        tint = colorResource(R.color.text_article_title)
                    )
                }
            }
        }

    }
    if (showBottomDialog) {
        ShareBottomDialog(onDismiss = { showBottomDialog = false }, onItemClick = {
            onShareClick?.invoke()
        })
    }
}

@Composable
fun CommonHeader(
    @DrawableRes leftIcon: Int?,
    @DrawableRes rightIcon: Int?,
    leftIconClick: (() -> Unit)?,
    rightIconClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (leftIcon != null) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 6.dp, 12.dp, 6.dp)
                    .clickable(onClick = {
                        leftIconClick?.invoke()
                    })
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leftIcon),
                    contentDescription = null,
                )
            }
        }
        content()
        if (rightIcon != null) {
            Box(
                modifier = Modifier
                    .padding(12.dp, 6.dp, 0.dp, 6.dp)
                    .clickable(onClick = {
                        rightIconClick?.invoke()
                    })
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = rightIcon),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CommonHeader(
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int?,
    @StringRes rightButtonText: Int?,
    leftIconClick: () -> Unit,
    rightButtonClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (leftIcon != null) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 6.dp, 12.dp, 6.dp)
                    .clickable(onClick = leftIconClick)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = leftIcon),
                    contentDescription = null,
                    tint = colorResource(R.color.text_article_title)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            content()
        }
        if (rightButtonText != null) {
            Text(
                text = stringResource(id = rightButtonText),
                fontSize = 16.sp,
                color = colorResource(id = R.color.blue_4285f4),
                modifier = Modifier.clickable(interactionSource = remember {
                    MutableInteractionSource()
                }, indication = null, onClick = rightButtonClick)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCommonHeader() {
    CommonHeader(
        title = "测试标题测试标题测试标题测试标题测试标题测试标题测试标题",
        leftIcon = R.drawable.ic_arrow_back,
        rightIcon = R.drawable.ic_more,
        leftIconClick = { /*TODO*/ })
}