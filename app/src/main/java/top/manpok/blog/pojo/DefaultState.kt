package top.manpok.blog.pojo

sealed class DefaultState {
    data object NONE : DefaultState()
    data object LOADING : DefaultState()
    data object EMPTY : DefaultState()
    data object NETWORK_ERROR : DefaultState()
}