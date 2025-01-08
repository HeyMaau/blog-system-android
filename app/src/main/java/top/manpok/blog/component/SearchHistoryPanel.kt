package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.db.entity.BlogSearchHistory

@Composable
fun SearchHistoryPanel(
    dataList: List<BlogSearchHistory>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(4.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.blue_1364e5),
                                colorResource(id = R.color.blue_0185fa)
                            )
                        ),
                        shape = RoundedCornerShape(30)
                    )
            )
            Text(
                text = stringResource(id = R.string.search_history),
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp),
                color = colorResource(R.color.text_article_title)
            )
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            if (dataList.size <= 6) {
                items(dataList) {
                    Text(
                        text = it.keyword,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable {
                                onItemClick(it.keyword)
                            }
                            .padding(vertical = 8.dp),
                        color = colorResource(R.color.text_article_title)
                    )
                }
            } else {
                items(6) {
                    Text(
                        text = dataList[it].keyword,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable {
                                onItemClick(dataList[it].keyword)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewSearchHistoryPanel() {
    /*SearchHistoryPanel(
        dataList = immutableListOf(
            BlogSearchHistory(
                keyword = "哈哈哈1",
                count = 1,
                updateTime = System.currentTimeMillis()
            ),
            BlogSearchHistory(
                keyword = "哈哈哈2",
                count = 1,
                updateTime = System.currentTimeMillis()
            ),
            BlogSearchHistory(
                keyword = "哈哈哈3",
                count = 1,
                updateTime = System.currentTimeMillis()
            ),
            BlogSearchHistory(
                keyword = "哈哈哈4",
                count = 1,
                updateTime = System.currentTimeMillis()
            ),
            BlogSearchHistory(
                keyword = "哈哈哈5",
                count = 1,
                updateTime = System.currentTimeMillis()
            )
        )
    )*/
}