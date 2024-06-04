package top.manpok.blog.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.SearchHistoryPanel
import top.manpok.blog.component.SearchInput
import top.manpok.blog.component.SearchResultItem
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.SearchViewModel

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val focusRequester = FocusRequester()
        setContent {
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
            val searchViewModel: SearchViewModel = viewModel()
            val density = LocalDensity.current
            val context = LocalContext.current
            val focusManager = LocalFocusManager.current
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                if (searchViewModel.beginSearch) {
                    LazyColumn(
                        modifier = Modifier
                            .background(colorResource(id = R.color.gray_f7f6fb))
                            .fillMaxSize()
                            .padding(top = searchViewModel.commonHeaderHeight)
                    ) {
                        if (searchViewModel.isLoading) {
                            items(8) {
                                SearchResultItem(
                                    title = null,
                                    content = null,
                                    cover = null,
                                    updateTime = null,
                                    useSkeleton = searchViewModel.isLoading
                                ) {}
                            }
                        } else {
                            items(searchViewModel.searchList) {
                                SearchResultItem(
                                    title = it?.title,
                                    content = it?.content,
                                    cover = Constants.BASE_IMAGE_URL + it?.cover,
                                    updateTime = it?.updateTime,
                                    useSkeleton = searchViewModel.isLoading,
                                    onClick = {
                                        val intent =
                                            Intent(context, ArticleDetailActivity::class.java)
                                        intent.putExtra(
                                            ArticleDetailActivity.INTENT_KEY_ARTICLE_ID,
                                            it?.id
                                        )
                                        context.startActivity(intent)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(top = searchViewModel.commonHeaderHeight + 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (searchViewModel.searchHistoryList.isNotEmpty()) {
                            SearchHistoryPanel(
                                dataList = searchViewModel.searchHistoryList.toList(),
                                onItemClick = {
                                    searchViewModel.keywords = it
                                    searchViewModel.beginSearch = true
                                    focusManager.clearFocus()
                                    searchViewModel.doSearch()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.logo_search),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                        )
                    }
                }
                CommonHeader(
                    leftIcon = R.drawable.ic_arrow_back,
                    rightButtonText = R.string.search,
                    leftIconClick = { finish() },
                    rightButtonClick = {
                        searchViewModel.beginSearch = true
                        searchViewModel.doSearch()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .background(Color.White)
                        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                        .onGloballyPositioned {
                            with(density) {
                                searchViewModel.commonHeaderHeight = it.size.height.toDp()
                            }
                        }
                ) {
                    SearchInput(
                        text = searchViewModel.keywords,
                        hint = stringResource(id = R.string.please_enter_search_keywords),
                        modifier = Modifier.focusRequester(focusRequester)
                    ) {
                        searchViewModel.keywords = it
                    }
                }
            }
        }
    }
}