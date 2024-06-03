package top.manpok.blog.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
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
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(colorResource(id = R.color.gray_f7f6fb))
                        .fillMaxSize()
                        .padding(top = searchViewModel.commonHeaderHeight)
                ) {
                    items(searchViewModel.searchList) {
                        SearchResultItem(
                            title = it?.title,
                            content = it?.content,
                            cover = Constants.BASE_IMAGE_URL + it?.cover,
                            updateTime = it?.updateTime,
                            onClick = {
                                val intent = Intent(context, ArticleDetailActivity::class.java)
                                intent.putExtra(ArticleDetailActivity.INTENT_KEY_ARTICLE_ID, it?.id)
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                CommonHeader(
                    leftIcon = R.drawable.ic_arrow_back,
                    rightButtonText = R.string.search,
                    leftIconClick = { finish() },
                    rightButtonClick = { searchViewModel.doSearch() },
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