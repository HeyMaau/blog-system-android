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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import top.manpok.blog.activity.CommonWebViewActivity
import top.manpok.blog.component.FriendLinkItem
import top.manpok.blog.viewmodel.FriendLinkViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToolsPage(
    modifier: Modifier = Modifier,
    friendLinkViewModel: FriendLinkViewModel = viewModel()
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 0.dp,
                    top = 20.dp,
                    end = 0.dp,
                    10.dp
                )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_tools),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(id = R.string.all_tools),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp, 0.dp),
                fontWeight = FontWeight(400)
            )
        }

        val pullRefreshState =
            rememberPullRefreshState(refreshing = friendLinkViewModel.refreshing, onRefresh = {
                friendLinkViewModel.refreshing = true
                friendLinkViewModel.getFriendLink(
                    friendLinkViewModel.currentPage,
                    friendLinkViewModel.pageSize
                )
            })

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.pullRefresh(pullRefreshState)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxSize()) {
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
                                    val intent = Intent(context, CommonWebViewActivity::class.java)
                                    intent.putExtra(CommonWebViewActivity.INTENT_KEY_URL, it.url)
                                    context.startActivity(intent)
                                }
                        )
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