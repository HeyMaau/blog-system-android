package top.manpok.blog.page

import android.graphics.Color
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.component.ArticleList
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.reachBottom
import top.manpok.blog.viewmodel.ArticleViewModel
import top.manpok.blog.viewmodel.BlogScaffoldViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeArticleListPage(
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null,
    listState: LazyListState = GlobalViewModelManager.listStateViewModel.homeArticleListState,
    articleViewModel: ArticleViewModel = viewModel(),
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel()
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = articleViewModel.refreshing, onRefresh = {
            refresh(articleViewModel)
        })

    LaunchedEffect(key1 = listState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD)) {
        if (listState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD)
            && !articleViewModel.loading && !articleViewModel.refreshing && !articleViewModel.noMore
        ) {
            articleViewModel.getArticleList(
                ++articleViewModel.currentPage,
                articleViewModel.pageSize
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        blogScaffoldViewModel.sameBottomItemClickIndex.collect {
            if (pagerState != null && pagerState.currentPage == 0) {
                when (it) {
                    0 -> {
                        if (!listState.canScrollBackward) {
                            refresh(articleViewModel)
                        } else {
                            listState.animateScrollToItem(0)
                        }
                        blogScaffoldViewModel.dispatchEvent(BlogScaffoldViewModel.ScaffoldIntent.FinishSameBottomItemClick)
                    }
                }
            }
        }
    }

    ArticleList(
        articleList = articleViewModel.articleList.toList(),
        refreshing = articleViewModel.refreshing,
        refreshState = pullRefreshState,
        listState = listState,
        modifier = modifier.pullRefresh(state = pullRefreshState, enabled = true)
    )
}

private fun refresh(articleViewModel: ArticleViewModel) {
    articleViewModel.refreshing = true
    articleViewModel.currentPage = Constants.DEFAULT_PAGE
    articleViewModel.pageSize = Constants.DEFAULT_PAGE_SIZE
    articleViewModel.noMore = false
    articleViewModel.getArticleList(articleViewModel.currentPage, articleViewModel.pageSize)
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreViewHomeArticleListPage() {
    HomeArticleListPage(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    )
}