package top.manpok.blog.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

@Composable
fun CategoryListItem(
    coverUrl: String,
    name: String,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            start = 0.dp,
            top = if (isFirst) 20.dp else 8.dp,
            end = 0.dp,
            bottom = if (isLast) 20.dp else 8.dp
        )
    ) {
        AsyncImage(
            model = coverUrl, contentDescription = null, modifier = Modifier
                .size(40.dp)
                .border(
                    width = 0.5.dp, color = colorResource(
                        id = R.color.gray_878789
                    ), shape = RoundedCornerShape(10)
                )
        )
        Text(text = name, fontSize = 16.sp, modifier = Modifier.padding(10.dp, 0.dp))
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCategoryListItem() {
    CategoryListItem(
        coverUrl = "https://manpok.top/image/417156442265485312",
        name = "Android",
        false,
        false
    )
}