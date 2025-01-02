package top.manpok.blog.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.lifecycle.ViewModel

class ListStateViewModel : ViewModel() {

    val homeArticleListState by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LazyListState()
    }

    val homeThinkingGridState by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LazyStaggeredGridState()
    }

    val categoryArticleListState by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LazyListState()
    }

}