package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var _sameBottomItemClickIndex = MutableStateFlow(-1)
    var sameBottomItemClickIndex = _sameBottomItemClickIndex.asStateFlow()

    fun dispatchEvent(scaffoldIntent: ScaffoldIntent) {
        when (scaffoldIntent) {
            is ScaffoldIntent.SameBottomItemClick -> {
                _sameBottomItemClickIndex.value = scaffoldIntent.index
            }

            ScaffoldIntent.FinishSameBottomItemClick -> {
                _sameBottomItemClickIndex.value = -1
            }
        }
    }

    sealed class ScaffoldIntent() {
        data class SameBottomItemClick(val index: Int) : ScaffoldIntent()
        data object FinishSameBottomItemClick : ScaffoldIntent()
    }
}