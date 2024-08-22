package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UpdateViewModel : ViewModel() {

    var showUpdateDialog: Boolean by mutableStateOf(true)
}