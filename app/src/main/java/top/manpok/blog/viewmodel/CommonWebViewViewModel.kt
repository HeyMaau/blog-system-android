package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CommonWebViewViewModel : ViewModel() {

    var title by mutableStateOf("")
    var progress by mutableIntStateOf(0)
}