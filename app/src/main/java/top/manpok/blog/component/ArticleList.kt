package top.manpok.blog.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R
import top.manpok.blog.activity.ArticleDetailActivity
import top.manpok.blog.activity.SearchActivity
import top.manpok.blog.pojo.BlogArticle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleList(
    articleList: List<BlogArticle.Data?>?,
    refreshing: Boolean = false,
    refreshState: PullRefreshState? = null,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    if (articleList != null) {
        Box(contentAlignment = Alignment.TopCenter, modifier = modifier) {
            LazyColumn(state = listState) {
                item {
                    BlogSearchBar(modifier = Modifier
                        .clickable {
                            val intent = Intent(context, SearchActivity::class.java)
                            context.startActivity(intent)
                        }
                        .padding(0.dp, 10.dp, 0.dp, 0.dp))
                }
                itemsIndexed(articleList, key = { _, item ->
                    item?.id!!
                }) { index, item ->
                    if (index == 0) {
                        ArticleListItem(
                            item = item,
                            isLast = index == articleList.size - 1,
                            click = {
                                val intent = Intent(context, ArticleDetailActivity::class.java)
                                intent.putExtra(
                                    ArticleDetailActivity.INTENT_KEY_ARTICLE_ID,
                                    item?.id
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                        )
                    } else {
                        ArticleListItem(
                            item = item,
                            isLast = index == articleList.size - 1,
                            click = {
                                val intent = Intent(context, ArticleDetailActivity::class.java)
                                intent.putExtra(
                                    ArticleDetailActivity.INTENT_KEY_ARTICLE_ID,
                                    item?.id
                                )
                                context.startActivity(intent)
                            })
                    }
                }
            }
            if (refreshState != null) {
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = refreshState,
                    backgroundColor = Color.White,
                    contentColor = colorResource(id = R.color.blue_4285f4)
                )
            }
        }
    }
}