package top.manpok.blog.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.manpok.blog.activity.SearchActivity
import top.manpok.blog.pojo.BlogArticle

@Composable
fun ArticleList(articleList: List<BlogArticle.Data?>?, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()
    if (articleList != null) {
        LazyColumn(state = listState, modifier = modifier) {
            item {
                val context = LocalContext.current
                BlogSearchBar(modifier = Modifier.clickable {
                    val intent = Intent(context, SearchActivity::class.java)
                    context.startActivity(intent)
                })
            }
            itemsIndexed(articleList) { index, item ->
                if (index == 0) {
                    ArticleListItem(
                        item = item,
                        isLast = index == articleList.size - 1,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                    )
                } else {
                    ArticleListItem(item = item, isLast = index == articleList.size - 1)
                }
            }
        }
    }
}