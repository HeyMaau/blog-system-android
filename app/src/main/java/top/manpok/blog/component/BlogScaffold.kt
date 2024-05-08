package top.manpok.blog.component

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.manpok.blog.R
import top.manpok.blog.pojo.BottomBarItem

val mBottomBarItemList = listOf(
    BottomBarItem(R.drawable.ic_home_filled, R.drawable.ic_home, R.string.home_page),
    BottomBarItem(R.drawable.ic_article_filled, R.drawable.ic_article, R.string.category_page),
    BottomBarItem(
        R.drawable.ic_tools_ladder_filled,
        R.drawable.ic_tools_ladder,
        R.string.tools_page
    ),
    BottomBarItem(R.drawable.ic_person_filled, R.drawable.ic_person, R.string.about_page)
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogScaffold(modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = {
            BlogNavigationBar(mBottomBarItemList)
        }
    ) {
    }
}