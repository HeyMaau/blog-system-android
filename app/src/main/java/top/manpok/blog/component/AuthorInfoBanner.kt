package top.manpok.blog.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

@Composable
fun AuthorInfoBanner(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    name: String,
    sign: String,
    showSkeleton: Boolean = false
) {
    if (showSkeleton) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = ColorPainter(colorResource(id = R.color.bg_cccccc)),
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(40.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = colorResource(id = R.color.bg_cccccc),
                    shape = RoundedCornerShape(40),
                    modifier = Modifier
                        .height(10.dp)
                        .width(80.dp)
                ) {}
                Surface(
                    color = colorResource(id = R.color.bg_cccccc),
                    shape = RoundedCornerShape(40),
                    modifier = Modifier
                        .height(10.dp)
                        .width(150.dp)
                ) {}
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(40.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    color = colorResource(R.color.text_article_title)
                )
                Text(
                    text = sign,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_category_author_name)
                )
            }
        }
    }

}

@Composable
fun AuthorInfoBanner(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    name: String,
    fontSize: TextUnit = 14.sp,
    @ColorRes fontColor: Int = R.color.gray_878789
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            color = colorResource(id = fontColor),
            modifier = Modifier.padding(5.dp, 0.dp),
            fontSize = fontSize
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewAuthorInfoBanner() {
    AuthorInfoBanner(
        avatarUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.b51d09e7b36090dc04e54de5fa7d989e?rik=QigJQWhXl6Z0Kg&riu=http%3a%2f%2fpic1.nipic.com%2f2009-02-25%2f200922520173452_2.jpg&ehk=WfbXwRrfc%2bwsfPtdUJ4vBvQe1zs7v5OkkD%2fDm2Rliqc%3d&risl=&pid=ImgRaw&r=0",
        name = "manpok",
        sign = "hhhhhhhh",
        showSkeleton = true
    )
}