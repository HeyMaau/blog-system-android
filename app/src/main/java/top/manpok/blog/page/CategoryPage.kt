package top.manpok.blog.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.ArticleListItem
import top.manpok.blog.component.CategoryInfoCard
import top.manpok.blog.component.CategoryPopup
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.ArticleViewModel
import top.manpok.blog.viewmodel.CategoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    articleViewModel: ArticleViewModel = viewModel(key = "CategoryPage")
) {
    DisposableEffect(key1 = categoryViewModel.currentIndex) {
        if (categoryViewModel.currentIndex != -1) {
            articleViewModel.getArticleListByCategory(
                articleViewModel.currentPage,
                articleViewModel.pageSize,
                categoryViewModel.categoryList[categoryViewModel.currentIndex].id
            )
        }
        onDispose { }
    }
    var showCategoryPopup by remember {
        mutableStateOf(false)
    }
    var commonHeaderHeight by remember {
        mutableIntStateOf(0)
    }
    LazyColumn(modifier = modifier) {
        stickyHeader {
            var headerHeight by remember {
                mutableIntStateOf(0)
            }
            CommonHeader(
                leftIcon = R.drawable.ic_arrow_back,
                rightIcon = R.drawable.ic_more,
                leftIconClick = { },
                rightIconClick = { },
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp, 0.dp)
                    .onGloballyPositioned {
                        commonHeaderHeight = it.size.height
                    }
            ) {
                Box {
                    Row(modifier = Modifier
                        .onGloballyPositioned {
                            headerHeight = it.size.height
                        }
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
                    CategoryPopup(show = showCategoryPopup, yOffset = headerHeight) {
                        showCategoryPopup = false
                    }
                }
            }
        }
        item {
            if (categoryViewModel.categoryList.isNotEmpty()) {
                CategoryInfoCard(
                    avatarUrl = Constants.BASE_IMAGE_URL + categoryViewModel.categoryList[0].cover,
                    userName = categoryViewModel.categoryList[0].name!!,
                    categoryName = categoryViewModel.categoryList[0].name!!,
                    categoryDesc = categoryViewModel.categoryList[0].description!!,
                    totalArticle = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 10.dp)
                )
            }
        }
        itemsIndexed(articleViewModel.articleList) { index, item ->
            ArticleListItem(
                item = item,
                isLast = index == articleViewModel.articleList.size - 1,
                click = { /*TODO*/ },
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
                .padding(0.dp, commonHeaderHeight.dp, 0.dp, 0.dp)
        ) {
        }
    }
}