package top.manpok.blog.page

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.ui.theme.NoRippleTheme
import top.manpok.blog.viewmodel.HomePageViewModel

private const val TAG = "HomePage"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    homePageViewModel: HomePageViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                divider = {},
                indicator = { tabPositions ->
                    val currentTabPosition = tabPositions[pagerState.currentPage]
                    if (pagerState.currentPage < tabPositions.size) {
                        val currentTabWidth by animateDpAsState(
                            targetValue = currentTabPosition.width / 4,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                        val indicatorOffset by animateDpAsState(
                            targetValue = currentTabPosition.left + (currentTabPosition.width / 2 - currentTabWidth / 2),
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        )
                        SecondaryIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomStart)
                                .offset(x = indicatorOffset)
                                .width(currentTabWidth),
                            height = 5.dp,
                            color = colorResource(id = R.color.blue_4285f4)
                        )
                    }
                },
                modifier = Modifier.padding(5.dp)
            ) {
                homePageViewModel.tabRowItemList.forEachIndexed { index, tabRowItem ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    }) {
                        Text(
                            text = stringResource(id = tabRowItem.title),
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }

        HorizontalPager(state = pagerState, beyondBoundsPageCount = 1) {
            when (it) {
                0 -> HomeArticleListPage(
                    pagerState = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp, 0.dp)
                )

                1 -> HomeThinkingListPage(
                    pagerState = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White, colorResource(id = R.color.gray_f6f7fb)
                                ),
                                endY = 50f
                            )
                        )
                )
            }
        }
    }

}