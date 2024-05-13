package top.manpok.blog.page

import android.content.Intent
import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.activity.SearchActivity
import top.manpok.blog.component.ArticleList
import top.manpok.blog.component.BlogSearchBar
import top.manpok.blog.viewmodel.ArticleViewModel

@Composable
fun HomeArticleListPage(
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel = viewModel()
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        BlogSearchBar(modifier = Modifier.clickable {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        })
        ArticleList(articleViewModel.articleList.toList(), modifier = Modifier.padding(0.dp, 10.dp))
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreViewHomeArticleListPage() {
    HomeArticleListPage(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 0.dp)
    )
}