package top.manpok.blog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.manpok.blog.base.BaseApplication.Companion.getApplication
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.utils.TempData


class EnvViewModel : ViewModel() {

    var currentEnv by mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            DataStoreManager.instance.getCurrentEnv(getApplication()).collect {
                currentEnv = it
                TempData.currentEnv = it
            }
        }
    }

    fun saveCurrentEnv(env: Int) {
        viewModelScope.launch {
            DataStoreManager.instance.setCurrentEnv(getApplication(), env)
        }
    }
}