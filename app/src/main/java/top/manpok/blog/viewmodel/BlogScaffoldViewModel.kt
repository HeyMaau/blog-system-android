package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import top.manpok.blog.R
import top.manpok.blog.pojo.BottomBarItem

class BlogScaffoldViewModel : ViewModel() {

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

    var selectedBottomItemIndex by mutableIntStateOf(0)

}