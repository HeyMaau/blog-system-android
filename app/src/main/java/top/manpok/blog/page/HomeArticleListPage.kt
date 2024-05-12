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
import top.manpok.blog.activity.SearchActivity
import top.manpok.blog.component.ArticleList
import top.manpok.blog.component.BlogSearchBar

@Composable
fun HomeArticleListPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        BlogSearchBar(modifier = Modifier.clickable {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        })
        ArticleList()
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