package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogFriendLink
import top.manpok.blog.pojo.DefaultState
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.NetworkUtil
import top.manpok.blog.utils.ToastUtil

class FriendLinkViewModel : ViewModel() {
    private val TAG = "FriendLinkViewModel"

    val friendLinkList = mutableStateListOf<BlogFriendLink.Data?>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(10)
    var total by mutableIntStateOf(0)

    var showSkeleton by mutableStateOf(true)
    var loadingTimeOut = false

    var refreshing by mutableStateOf(false)

    private var _friendLinkState = MutableStateFlow<DefaultState>(DefaultState.NONE)
    var friendLinkState = _friendLinkState.asStateFlow()

    init {
        getFriendLink(currentPage, pageSize)
        viewModelScope.launch {
            delay(500L)
            if (friendLinkList.isNotEmpty()) {
                showSkeleton = false
            }
            loadingTimeOut = true
        }
    }

    fun getFriendLink(page: Int, size: Int) {
        if (!NetworkUtil.isNetworkAvailable(BaseApplication.getApplication())) {
            _friendLinkState.value = DefaultState.NETWORK_ERROR
            return
        }
        _friendLinkState.value = DefaultState.NONE
        BlogRetrofit.friendLinkApi.getFriendLinkList(page, size).enqueue(object :
            Callback<BaseResponse<BlogFriendLink>> {
            override fun onResponse(
                call: Call<BaseResponse<BlogFriendLink>>,
                response: Response<BaseResponse<BlogFriendLink>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == Constants.CODE_SUCCESS) {
                        val data = body.data
                        currentPage = data?.currentPage!!
                        noMore = data.noMore
                        pageSize = data.pageSize
                        total = data.total
                        friendLinkList.clear()
                        data.data?.let {
                            friendLinkList.addAll(it)
                            if (loadingTimeOut) {
                                showSkeleton = false
                            }
                        }
                        if (refreshing) {
                            ToastUtil.showShortToast(R.string.refresh_successfully)
                        }
                    }
                }
                refreshing = false
            }

            override fun onFailure(call: Call<BaseResponse<BlogFriendLink>>, error: Throwable) {
                if (refreshing) {
                    ToastUtil.showShortToast(R.string.refresh_fail)
                }
                if (loadingTimeOut) {
                    showSkeleton = false
                }
                refreshing = false
                _friendLinkState.value = DefaultState.NETWORK_ERROR
                LogUtil.e(TAG, "onFailure: $error")
            }
        })
    }
}