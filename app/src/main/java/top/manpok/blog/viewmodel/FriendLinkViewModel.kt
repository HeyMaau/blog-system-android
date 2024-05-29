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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogFriendLink
import top.manpok.blog.utils.Constants

class FriendLinkViewModel : ViewModel() {
    private val TAG = "FriendLinkViewModel"

    val friendLinkList = mutableStateListOf<BlogFriendLink.Data?>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(10)
    var total by mutableIntStateOf(0)

    var showSkeleton by mutableStateOf(true)

    init {
        getFriendLink(currentPage, pageSize)
        viewModelScope.launch {
            delay(500L)
            if (!friendLinkList.isEmpty()) {
                showSkeleton = false
            }
        }
    }

    fun getFriendLink(page: Int, size: Int) {
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
                            showSkeleton = false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<BlogFriendLink>>, error: Throwable) {
                Log.d(TAG, "onFailure: $error")
            }
        })
    }
}