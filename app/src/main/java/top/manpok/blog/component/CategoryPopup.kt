package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.manpok.blog.pojo.BlogCategory
import top.manpok.blog.utils.Constants

@Composable
fun CategoryPopup(
    dataList: List<BlogCategory>,
    yOffset: Dp = 0.dp,
    alpha: Float = 1.0f,
    onClick: (index: Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter, modifier = Modifier
            .offset(y = yOffset)
            .alpha(alpha)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            dataList.forEachIndexed { index, blogCategory ->
                CategoryListItem(
                    coverUrl = Constants.BASE_IMAGE_URL + blogCategory.cover,
                    name = blogCategory.name!!,
                    isFirst = index == 0,
                    isLast = index == dataList.size - 1,
                    click = {
                        onClick?.invoke(index)
                    }
                )
                if (index != dataList.size - 1) {
                    HorizontalDivider(modifier = Modifier.width(200.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCategoryPopup() {
}