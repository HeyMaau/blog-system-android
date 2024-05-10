package top.manpok.blog.viewmodel

import androidx.lifecycle.ViewModel
import top.manpok.blog.R
import top.manpok.blog.pojo.TabRowItem

class HomePageViewModel : ViewModel() {

    val tabRowItemList = listOf(
        TabRowItem(R.string.recommend),
        TabRowItem(R.string.thinking)
    )
}