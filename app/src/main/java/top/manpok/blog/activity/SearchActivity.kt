package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.SearchInput
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
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp)
            ) {
                CommonHeader(
                    leftIcon = R.drawable.ic_arrow_back,
                    rightButtonText = R.string.search,
                    leftIconClick = { finish() },
                    rightButtonClick = { /*TODO*/ }) {
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