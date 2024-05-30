package top.manpok.blog.page

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.component.ArticleList
import top.manpok.blog.viewmodel.ArticleViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeArticleListPage(
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel = viewModel(key = "HomeArticleListPage")
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = articleViewModel.refreshing, onRefresh = {
            articleViewModel.refreshing = true
            articleViewModel.getArticleList(articleViewModel.currentPage, articleViewModel.pageSize)
        })
    ArticleList(
        articleList = articleViewModel.articleList.toList(),
        refreshing = articleViewModel.refreshing,
        refreshState = pullRefreshState,
        modifier = modifier.pullRefresh(state = pullRefreshState, enabled = true)
    )
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreViewHomeArticleListPage() {
    HomeArticleListPage(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    )
}