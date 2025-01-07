package top.manpok.blog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R

@Composable
fun CategoryInfoCard(
    avatarUrl: String,
    userName: String,
    coverUrl: String,
    categoryName: String,
    categoryDesc: String,
    totalArticle: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardColors(
            containerColor = colorResource(R.color.bg_white),
            contentColor = Color.Unspecified,
            disabledContentColor = colorResource(R.color.bg_white),
            disabledContainerColor = colorResource(R.color.bg_white)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(15.dp, 15.dp)) {
                AuthorInfoBanner(
                    avatarUrl = avatarUrl,
                    name = userName,
                    fontSize = 16.sp,
                    fontColor = R.color.text_category_author_name
                )
                Text(
                    text = categoryName,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp),
                    color = colorResource(R.color.text_article_title)
                )
                Text(text = categoryDesc, color = colorResource(R.color.text_article_title))
                Text(
                    text = stringResource(id = R.string.category_total_article, totalArticle),
                    modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 0.dp),
                    color = colorResource(R.color.text_article_title)
                )
            }
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(20.dp, 0.dp)
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCategoryInfoCard() {
    CategoryInfoCard(
        avatarUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.b51d09e7b36090dc04e54de5fa7d989e?rik=QigJQWhXl6Z0Kg&riu=http%3a%2f%2fpic1.nipic.com%2f2009-02-25%2f200922520173452_2.jpg&ehk=WfbXwRrfc%2bwsfPtdUJ4vBvQe1zs7v5OkkD%2fDm2Rliqc%3d&risl=&pid=ImgRaw&r=0",
        userName = "manpok",
        coverUrl = "",
        categoryName = "Android",
        categoryDesc = "卷不完的机器人",
        totalArticle = 10
    )
}