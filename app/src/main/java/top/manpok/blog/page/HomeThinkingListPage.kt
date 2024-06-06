package top.manpok.blog.page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.activity.ThinkingDetailActivity
import top.manpok.blog.component.ThinkingListItem
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.reachBottom
import top.manpok.blog.viewmodel.ThinkingViewModel

private const val TAG = "HomeThinkingListPage"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeThinkingListPage(
    modifier: Modifier = Modifier,
    thinkingViewModel: ThinkingViewModel = viewModel()
) {
    val context = LocalContext.current
    val pullRefreshState =
        rememberPullRefreshState(refreshing = thinkingViewModel.refreshing, onRefresh = {
            thinkingViewModel.refreshing = true
            thinkingViewModel.currentPage = Constants.DEFAULT_PAGE
            thinkingViewModel.pageSize = Constants.DEFAULT_PAGE_SIZE
            thinkingViewModel.getThinkingList(
                thinkingViewModel.currentPage,
                thinkingViewModel.pageSize
            )
        })

    val staggeredGridState = rememberLazyStaggeredGridState()
    LaunchedEffect(key1 = staggeredGridState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD)) {
        if (staggeredGridState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD)
            && !thinkingViewModel.loading && !thinkingViewModel.refreshing && !thinkingViewModel.noMore
        ) {
            thinkingViewModel.getThinkingList(
                ++thinkingViewModel.currentPage,
                thinkingViewModel.pageSize
            )
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.pullRefresh(state = pullRefreshState, enabled = true)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = staggeredGridState
        ) {
            items(thinkingViewModel.thinkingList.toList(), key = {
                it.id!!
            }) {
                ThinkingListItem(data = it, click = {
                    val intent = Intent(context, ThinkingDetailActivity::class.java)
                    intent.putExtra(
                        ThinkingDetailActivity.INTENT_KEY_AUTHOR_NAME,
                        it.user?.userName
                    )
                    intent.putExtra(ThinkingDetailActivity.INTENT_KEY_AUTHOR_SIGN, it.user?.sign)
                    intent.putExtra(
                        ThinkingDetailActivity.INTENT_KEY_AUTHOR_AVATAR,
                        it.user?.avatar
                    )
                    intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_TITLE, it.title)
                    intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_CONTENT, it.content)
                    intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_IMAGES, it.images)
                    intent.putExtra(
                        ThinkingDetailActivity.INTENT_KEY_THINKING_UPDATE_TIME,
                        it.updateTime
                    )
                    context.startActivity(intent)
                })
            }
        }
        PullRefreshIndicator(
            refreshing = thinkingViewModel.refreshing,
            state = pullRefreshState,
            backgroundColor = Color.White,
            contentColor = colorResource(id = R.color.blue_4285f4)
        )
    }
}