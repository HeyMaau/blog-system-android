package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogCategory
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil

class CategoryViewModel : ViewModel() {
    private val TAG = "CategoryViewModel"

    val categoryList = mutableStateListOf<BlogCategory>()
    var currentIndex by mutableIntStateOf(-1)
    var refreshing by mutableStateOf(false)

    var commonHeaderHeight by mutableStateOf(0.dp)

    private val _refreshState = MutableStateFlow<RefreshState>(RefreshState.Stop)
    val refreshState = _refreshState.asStateFlow()

    init {
        getCategoryList()
    }

    fun getCategoryList() {
        if (refreshing) {
            _refreshState.value = RefreshState.Refreshing
        }
        BlogRetrofit.categoryApi.getCategoryList()
            .enqueue(object : Callback<BaseResponse<List<BlogCategory>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<BlogCategory>>>,
                    response: Response<BaseResponse<List<BlogCategory>>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.code == Constants.CODE_SUCCESS) {
                            val data = body.data
                            if (data != null) {
                                categoryList.clear()
                                categoryList.addAll(data)
                                if (currentIndex == -1) {
                                    currentIndex = 0
                                }
                            }
                        }
                    }
                    if (refreshing) {
                        _refreshState.value = RefreshState.Finish
                    }
                    refreshing = false
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<BlogCategory>>>,
                    error: Throwable
                ) {
                    if (refreshing) {
                        _refreshState.value = RefreshState.Error
                    }
                    refreshing = false
                    LogUtil.e(TAG, "onFailure: $error")
                }

            })
    }

    sealed class RefreshState {
        data object Stop : RefreshState()
        data object Refreshing : RefreshState()
        data object Finish : RefreshState()
        data object Error : RefreshState()
    }
}