package top.manpok.blog.viewmodel

object GlobalViewModelManager {

    val audioViewModel: AudioViewModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        AudioViewModel()
    }
}