package top.manpok.blog.page

import android.content.Intent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.activity.ArticleDetailActivity
import top.manpok.blog.component.ArticleListItem
import top.manpok.blog.component.CategoryInfoCard
import top.manpok.blog.component.CategoryPopup
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.reachBottom
import top.manpok.blog.viewmodel.ArticleCategoryViewModel
import top.manpok.blog.viewmodel.BlogScaffoldViewModel
import top.manpok.blog.viewmodel.CategoryViewModel
import top.manpok.blog.viewmodel.UserViewModel

private const val TAG = "CategoryPage"
private const val ANIMATION_FIRST_STAGE_TIME = 300
private const val ANIMATION_SECOND_STAGE_TIME = 200

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    articleCategoryViewModel: ArticleCategoryViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        categoryViewModel.refreshState.collect {
            if (it is CategoryViewModel.RefreshState.Finish) {
                articleCategoryViewModel.currentPage = Constants.DEFAULT_PAGE
                articleCategoryViewModel.pageSize = Constants.DEFAULT_PAGE_SIZE
                articleCategoryViewModel.getArticleListByCategory(
                    articleCategoryViewModel.currentPage,
                    articleCategoryViewModel.pageSize,
                    categoryViewModel.categoryList[categoryViewModel.currentIndex].id
                )
            }
        }
    }
    DisposableEffect(key1 = categoryViewModel.currentIndex) {
        if (categoryViewModel.currentIndex != -1) {
            articleCategoryViewModel.refreshing = true
            articleCategoryViewModel.pureLoading = true
            articleCategoryViewModel.currentPage = Constants.DEFAULT_PAGE
            articleCategoryViewModel.pageSize = Constants.DEFAULT_PAGE_SIZE
            articleCategoryViewModel.noMore = false
            articleCategoryViewModel.getArticleListByCategory(
                articleCategoryViewModel.currentPage,
                articleCategoryViewModel.pageSize,
                categoryViewModel.categoryList[categoryViewModel.currentIndex].id
            )
        }
        onDispose { }
    }
    var showCategoryPopup by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val density = LocalDensity.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = articleCategoryViewModel.refreshing,
        onRefresh = {
            categoryViewModel.refreshing = true
            articleCategoryViewModel.refreshing = true
            articleCategoryViewModel.pureLoading = false
            articleCategoryViewModel.lastCategoryID = ""
            articleCategoryViewModel.noMore = false
            categoryViewModel.getCategoryList()
        })

    var popupState: PopupState by remember {
        mutableStateOf(PopupState.STOP)
    }
    val updateTransition = updateTransition(targetState = popupState, label = "popup_transition")
    val categoryPopupY by updateTransition.animateDp(transitionSpec = {
        when {
            PopupState.STOP isTransitioningTo PopupState.OPEN_FIRST_STAGE -> tween(durationMillis = ANIMATION_FIRST_STAGE_TIME)
            PopupState.OPEN_FIRST_STAGE isTransitioningTo PopupState.OPEN_SECOND_STAGE -> tween(
                durationMillis = ANIMATION_SECOND_STAGE_TIME,
                easing = FastOutLinearInEasing
            )

            PopupState.OPEN_SECOND_STAGE isTransitioningTo PopupState.CLOSE_FIRST_STAGE -> tween(
                durationMillis = ANIMATION_FIRST_STAGE_TIME
            )

            PopupState.CLOSE_FIRST_STAGE isTransitioningTo PopupState.STOP -> tween(
                durationMillis = ANIMATION_SECOND_STAGE_TIME,
                easing = FastOutLinearInEasing
            )

            else -> {
                tween(500)
            }
        }
    }, label = "transition_y") {
        when (it) {
            PopupState.STOP -> -categoryViewModel.commonHeaderHeight
            PopupState.OPEN_FIRST_STAGE -> categoryViewModel.commonHeaderHeight
            PopupState.OPEN_SECOND_STAGE -> 0.dp
            PopupState.CLOSE_FIRST_STAGE -> categoryViewModel.commonHeaderHeight
        }
    }
    val categoryPopupAlpha by updateTransition.animateFloat(transitionSpec = {
        when {
            PopupState.STOP isTransitioningTo PopupState.OPEN_FIRST_STAGE -> tween(durationMillis = ANIMATION_FIRST_STAGE_TIME)
            PopupState.CLOSE_FIRST_STAGE isTransitioningTo PopupState.STOP -> tween(durationMillis = ANIMATION_SECOND_STAGE_TIME)
            else -> {
                tween(durationMillis = ANIMATION_FIRST_STAGE_TIME)
            }
        }

    }, label = "transition_alpha") {
        when (it) {
            PopupState.STOP -> 0.0f
            PopupState.OPEN_FIRST_STAGE -> 1.0f
            PopupState.OPEN_SECOND_STAGE -> 1.0f
            PopupState.CLOSE_FIRST_STAGE -> 1.0f
        }
    }
    LaunchedEffect(key1 = updateTransition.currentState) {
        when (updateTransition.currentState) {
            PopupState.OPEN_FIRST_STAGE -> popupState = PopupState.OPEN_SECOND_STAGE
            PopupState.CLOSE_FIRST_STAGE -> popupState = PopupState.STOP
            PopupState.STOP -> showCategoryPopup = false
            else -> {}
        }
    }

    val listState = rememberLazyListState()
    LaunchedEffect(key1 = listState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD)) {
        if (listState.reachBottom(Constants.AUTO_LOAD_MORE_THRESHOLD) && !articleCategoryViewModel.noMore
            && !articleCategoryViewModel.refreshing && !articleCategoryViewModel.pureLoading
        ) {
            articleCategoryViewModel.getArticleListByCategory(
                ++articleCategoryViewModel.currentPage,
                articleCategoryViewModel.pageSize,
                articleCategoryViewModel.lastCategoryID,
                true
            )
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.pullRefresh(state = pullRefreshState, enabled = true)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = categoryViewModel.commonHeaderHeight)
                .fillMaxSize(),
            state = listState
        ) {
            item {
                if (categoryViewModel.categoryList.isNotEmpty()) {
                    CategoryInfoCard(
                        avatarUrl = userViewModel.authorAvatar,
                        userName = userViewModel.authorName,
                        coverUrl = Constants.BASE_IMAGE_URL + categoryViewModel.categoryList[categoryViewModel.currentIndex].cover,
                        categoryName = categoryViewModel.categoryList[categoryViewModel.currentIndex].name!!,
                        categoryDesc = categoryViewModel.categoryList[categoryViewModel.currentIndex].description!!,
                        totalArticle = articleCategoryViewModel.total,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 10.dp)
                    )
                }
            }
            itemsIndexed(items = articleCategoryViewModel.articleList, key = { _, item ->
                item?.id!!
            }) { index, item ->
                ArticleListItem(
                    item = item,
                    isLast = index == articleCategoryViewModel.articleList.size - 1,
                    click = {
                        val intent = Intent(context, ArticleDetailActivity::class.java)
                        intent.putExtra(ArticleDetailActivity.INTENT_KEY_ARTICLE_ID, item?.id)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(12.dp, 0.dp)
                )
            }
        }
        if (showCategoryPopup) {
            Surface(
                color = colorResource(id = R.color.gray_77000000),
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .alpha(categoryPopupAlpha)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            popupState = PopupState.CLOSE_FIRST_STAGE
                        })
            ) {
            }
        }
        Column {
            CommonHeader(
                leftIcon = R.drawable.ic_arrow_back,
                rightIcon = R.drawable.ic_more,
                leftIconClick = {
                    blogScaffoldViewModel.selectedBottomItemIndex = 0
                },
                rightIconClick = { },
                modifier = Modifier
                    .zIndex(1f)
                    .background(Color.White)
                    .padding(12.dp, 0.dp)
                    .onGloballyPositioned {
                        with(density) {
                            categoryViewModel.commonHeaderHeight = it.size.height.toDp()
                        }
                    }
            ) {
                Row(modifier = Modifier
                    .clickable(interactionSource = remember {
                        MutableInteractionSource()
                    }, indication = null) {
                        if (!showCategoryPopup) {
                            showCategoryPopup = true
                        }
                        when (popupState) {
                            PopupState.STOP -> popupState = PopupState.OPEN_FIRST_STAGE
                            PopupState.OPEN_SECOND_STAGE -> popupState =
                                PopupState.CLOSE_FIRST_STAGE

                            else -> {}
                        }
                    }) {
                    Text(
                        text = if (categoryViewModel.currentIndex != -1) categoryViewModel.categoryList[categoryViewModel.currentIndex].name!! else stringResource(
                            id = R.string.default_category_android
                        )
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_drop_down),
                        contentDescription = null
                    )
                }
            }
            if (showCategoryPopup) {
                CategoryPopup(
                    dataList = categoryViewModel.categoryList,
                    yOffset = categoryPopupY,
                    alpha = categoryPopupAlpha
                ) {
                    categoryViewModel.currentIndex = it
                    popupState = PopupState.CLOSE_FIRST_STAGE
                }
            }
        }
        PullRefreshIndicator(
            refreshing = articleCategoryViewModel.refreshing,
            state = pullRefreshState,
            backgroundColor = Color.White,
            contentColor = colorResource(id = R.color.blue_4285f4)
        )
    }
}

sealed class PopupState {
    data object STOP : PopupState()
    data object OPEN_FIRST_STAGE : PopupState()
    data object OPEN_SECOND_STAGE : PopupState()
    data object CLOSE_FIRST_STAGE : PopupState()
}