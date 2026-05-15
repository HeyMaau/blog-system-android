package top.manpok.blog.page

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.activity.AudioPlayerActivity
import top.manpok.blog.activity.CounterActivity
import top.manpok.blog.activity.CommonWebViewActivity
import top.manpok.blog.component.ActivityEntranceItem
import top.manpok.blog.component.DefaultUIState
import top.manpok.blog.component.FriendLinkItem
import top.manpok.blog.pojo.DefaultState
import top.manpok.blog.viewmodel.BlogScaffoldViewModel
import top.manpok.blog.viewmodel.FriendLinkViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToolsPage(
    modifier: Modifier = Modifier,
    friendLinkViewModel: FriendLinkViewModel = viewModel(),
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel()
) {
    val context = LocalContext.current

    var uiState: DefaultState by remember {
        mutableStateOf(DefaultState.NONE)
    }

    LaunchedEffect(key1 = Unit) {
        friendLinkViewModel.friendLinkState.collect {
            uiState = it
        }
    }

    val pullRefreshState =
        rememberPullRefreshState(refreshing = friendLinkViewModel.refreshing, onRefresh = {
            refresh(friendLinkViewModel)
        })

    LaunchedEffect(key1 = Unit) {
        blogScaffoldViewModel.sameBottomItemClickIndex.collect {
            when (it) {
                2 -> {
                    refresh(friendLinkViewModel)
                    blogScaffoldViewModel.dispatchEvent(BlogScaffoldViewModel.ScaffoldIntent.FinishSameBottomItemClick)
                }
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 0.dp,
                    top = 20.dp,
                    end = 0.dp,
                    bottom = 10.dp
                )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_tools),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = colorResource(R.color.text_article_title)
            )
            Text(
                text = stringResource(id = R.string.all_tools),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp, 0.dp),
                fontWeight = FontWeight(400),
                color = colorResource(R.color.text_article_title)
            )
        }

        // Scrollable content with pull-to-refresh
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .weight(1f)
                .pullRefresh(pullRefreshState)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()) {
                // Local tools section header
                item(span = { GridItemSpan(3) }) {
                    Text(
                        text = stringResource(id = R.string.tools_local),
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(R.color.text_article_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )
                }

                // Local tool items
                item {
                    ActivityEntranceItem(
                        showSkeleton = false,
                        logo = R.drawable.ic_audio_player,
                        name = stringResource(id = R.string.audio_player_entrance),
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp)
                            .clickable {
                                val intent =
                                    Intent(context, AudioPlayerActivity::class.java)
                                context.startActivity(intent)
                            }
                    )
                }
                item {
                    ActivityEntranceItem(
                        showSkeleton = false,
                        logo = R.drawable.ic_counter,
                        name = stringResource(R.string.counter),
                        modifier = Modifier
                            .padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp)
                            .clickable {
                                val intent =
                                    Intent(context, CounterActivity::class.java)
                                context.startActivity(intent)
                            }
                    )
                }

                // Online tools section header
                item(span = { GridItemSpan(3) }) {
                    Text(
                        text = stringResource(id = R.string.tools_online),
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(R.color.text_article_title),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                    )
                }

                // Online tools content
                if (uiState == DefaultState.NETWORK_ERROR) {
                    item(span = { GridItemSpan(3) }) {
                        DefaultUIState(
                            state = uiState,
                            hint = stringResource(id = R.string.network_error),
                            onClick = {
                                refresh(friendLinkViewModel)
                            }
                        )
                    }
                } else {
                    if (friendLinkViewModel.showSkeleton) {
                        items(9) {
                            FriendLinkItem(
                                showSkeleton = friendLinkViewModel.showSkeleton,
                                logoUrl = null,
                                name = ""
                            )
                        }
                    } else {
                        items(friendLinkViewModel.friendLinkList) {
                            FriendLinkItem(
                                showSkeleton = friendLinkViewModel.showSkeleton,
                                logoUrl = it?.logo,
                                name = it?.name!!,
                                modifier = Modifier
                                    .padding(
                                        start = 12.dp,
                                        top = 16.dp,
                                        end = 12.dp,
                                        bottom = 16.dp
                                    )
                                    .clickable {
                                        val intent =
                                            Intent(context, CommonWebViewActivity::class.java)
                                        intent.putExtra(
                                            CommonWebViewActivity.INTENT_KEY_URL,
                                            it.url
                                        )
                                        context.startActivity(intent)
                                    }
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = friendLinkViewModel.refreshing,
                state = pullRefreshState,
                backgroundColor = Color.White,
                contentColor = colorResource(id = R.color.blue_4285f4)
            )
        }
    }
}

private fun refresh(friendLinkViewModel: FriendLinkViewModel) {
    friendLinkViewModel.refreshing = true
    friendLinkViewModel.getFriendLink(
        friendLinkViewModel.currentPage,
        friendLinkViewModel.pageSize
    )
}
