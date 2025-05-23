package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

private val CONTENT_HEIGHT = 70.dp

@Composable
fun SearchResultItem(
    modifier: Modifier = Modifier,
    keyword: String,
    titleList: List<String>?,
    contentList: List<String>?,
    cover: String?,
    updateTime: String?,
    useSkeleton: Boolean = false,
    onClick: () -> Unit
) {
    if (useSkeleton) {
        Column(
            modifier
                .background(colorResource(R.color.bg_white))
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
                .clickable(onClick = onClick)
        ) {
            Surface(
                color = colorResource(id = R.color.bg_cccccc),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .padding(vertical = 8.dp)
            ) {}
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(2f)
                        .height(CONTENT_HEIGHT)
                ) {
                    for (i in 0..1) {
                        Surface(
                            color = colorResource(id = R.color.bg_cccccc),
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .padding(top = 8.dp),
                        ) {}
                    }
                    Surface(
                        modifier = Modifier
                            .width(100.dp)
                            .height(16.dp)
                            .padding(top = 8.dp),
                        shape = CircleShape,
                        color = colorResource(
                            id = R.color.bg_cccccc
                        )
                    ) {}
                }
                Surface(
                    color = colorResource(id = R.color.bg_cccccc),
                    modifier = Modifier
                        .weight(1f)
                        .height(CONTENT_HEIGHT)
                        .padding(start = 12.dp)
                        .clip(RoundedCornerShape(10))
                ) {}
            }
        }
    } else {
        Column(
            modifier
                .background(colorResource(R.color.bg_white))
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = buildAnnotatedString {
                    titleList?.forEachIndexed { index, s ->
                        append(s)
                        if (index != titleList.size - 1 && s != "。" && s != "，" && s != "、" && s != "；" && s != "." && s != "," && s != ";" && s != "\'") {
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_ff8c00))) {
                                append(keyword)
                            }
                        }
                    }
                },
                fontSize = 16.sp,
                lineHeight = 1.2.em,
                fontWeight = FontWeight(550),
                modifier = Modifier.padding(vertical = 8.dp),
                color = colorResource(R.color.text_article_title)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(2f)
                        .height(CONTENT_HEIGHT)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            contentList?.forEachIndexed { index, s ->
                                append(s)
                                if (index != contentList.size - 1 && s != "。" && s != "，" && s != "、" && s != "；" && s != "." && s != "," && s != ";" && s != "\'") {
                                    withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_ff8c00))) {
                                        append(keyword)
                                    }
                                }
                            }
                        },
                        maxLines = 2,
                        fontSize = 12.sp,
                        lineHeight = 1.5.em,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(R.color.text_article_summary)
                    )
                    Text(
                        text = updateTime ?: "",
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 12.sp,
                        color = colorResource(
                            id = R.color.text_article_summary
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
}