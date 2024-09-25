package top.manpok.blog.view

object AudioFloatingWindowManager {

    var expandState: ExpandState = ExpandState.Collapse

    sealed class ExpandState {
        data object Expand : ExpandState()
        data object Collapse : ExpandState()
    }
}
