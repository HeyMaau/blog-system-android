package top.manpok.blog.page

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import top.manpok.blog.viewmodel.ArticleCategoryViewModel
import top.manpok.blog.viewmodel.CategoryViewModel
import top.manpok.blog.viewmodel.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    articleCategoryViewModel: ArticleCategoryViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
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
            articleCategoryViewModel.currentPage = Constants.DEFAULT_PAGE
            articleCategoryViewModel.pageSize = Constants.DEFAULT_PAGE_SIZE
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
    var commonHeaderHeight by remember {
        mutableStateOf(0.dp)
    }
    val context = LocalContext.current
    val density = LocalDensity.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = articleCategoryViewModel.refreshing,
        onRefresh = {
            categoryViewModel.refreshing = true
            articleCategoryViewModel.refreshing = true
            articleCategoryViewModel.lastCategoryID = ""
            categoryViewModel.getCategoryList()
        })
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.pullRefresh(state = pullRefreshState, enabled = true)
    ) {
        LazyColumn(modifier = Modifier.padding(top = commonHeaderHeight)) {
            item {
                if (categoryViewModel.categoryList.isNotEmpty()) {
                    CategoryInfoCard(
                        avatarUrl = userViewModel.authorAvatar,
                        userName = userViewModel.authorName,
                        coverUrl = Constants.BASE_IMAGE_URL + categoryViewModel.categoryList[categoryViewModel.currentIndex].cover,
                        categoryName = categoryViewModel.categoryList[categoryViewModel.currentIndex].name!!,
                        categoryDesc = categoryViewModel.categoryList[categoryViewModel.currentIndex].description!!,
                        totalArticle = 4,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp, 10.dp)
                    )
                }
            }
            itemsIndexed(articleCategoryViewModel.articleList) { index, item ->
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
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
            }
        }
        Column {
            CommonHeader(
                leftIcon = R.drawable.ic_arrow_back,
                rightIcon = R.drawable.ic_more,
                leftIconClick = { },
                rightIconClick = { },
                modifier = Modifier.zIndex(1f)
                    .background(Color.White)
                    .padding(12.dp, 0.dp)
                    .onGloballyPositioned {
                        with(density) {
                            commonHeaderHeight = it.size.height.toDp()
                        }
                    }
            ) {
                Row(modifier = Modifier
                    .clickable {
                        showCategoryPopup = !showCategoryPopup
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
                CategoryPopup(dataList = categoryViewModel.categoryList, yOffset = 0.dp) {
                    categoryViewModel.currentIndex = it
                    showCategoryPopup = false
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