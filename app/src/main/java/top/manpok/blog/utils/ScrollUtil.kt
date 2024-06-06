package top.manpok.blog.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState

fun LazyListState.reachBottom(threshold: Int = 0): Boolean {
    val lastVisibleItemInfo = this.layoutInfo.visibleItemsInfo.lastOrNull()
    if (lastVisibleItemInfo == null) {
        return true
    } else {
        return lastVisibleItemInfo.index >= this.layoutInfo.totalItemsCount - threshold
    }
}

fun LazyStaggeredGridState.reachBottom(threshold: Int = 0): Boolean {
    val lastVisibleItemInfo = this.layoutInfo.visibleItemsInfo.lastOrNull()
    if (lastVisibleItemInfo == null) {
        return true
    } else {
        return lastVisibleItemInfo.index >= this.layoutInfo.totalItemsCount - threshold
    }
}