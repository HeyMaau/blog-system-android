package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

private val CONTENT_HEIGHT = 70.dp

@Composable
fun SearchResultItem(
    modifier: Modifier = Modifier,
    title: String?,
    content: String?,
    cover: String?,
    updateTime: String?,
    onClick: () -> Unit
) {
    Column(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title ?: "",
            fontSize = 16.sp,
            lineHeight = 1.2.em,
            fontWeight = FontWeight(550),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(2f)
                    .height(CONTENT_HEIGHT)
            ) {
                Text(
                    text = content ?: "",
                    maxLines = 2,
                    fontSize = 12.sp,
                    lineHeight = 1.5.em,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = updateTime ?: "",
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 12.sp,
                    color = colorResource(
                        id = R.color.gray_cccccc
                    )
                )
            }
            AsyncImage(
                model = cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .height(CONTENT_HEIGHT)
                    .padding(start = 12.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(10))
                    .clip(RoundedCornerShape(10))
            )
        }
    }
}