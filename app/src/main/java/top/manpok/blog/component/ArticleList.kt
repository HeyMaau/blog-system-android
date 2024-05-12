package top.manpok.blog.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.viewmodel.ArticleViewModel

@Composable
fun ArticleList(modifier: Modifier = Modifier, articleViewModel: ArticleViewModel = viewModel()) {
    Text(text = "ArticleList")
    LaunchedEffect(key1 = Unit) {
        articleViewModel.getArticleList(1, 5)
    }
    LazyColumn {

    }
}