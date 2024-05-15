package top.manpok.blog.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogThinking
import top.manpok.blog.utils.Constants

class ThinkingViewModel : ViewModel() {

    private val TAG = "ThinkingViewModel"

    val thinkingList = mutableStateListOf<BlogThinking.Data>()
    var currentPage by mutableIntStateOf(1)
    var noMore by mutableStateOf(false)
    var pageSize by mutableIntStateOf(5)
    var total by mutableIntStateOf(0)

    init {
        getThinkingList(currentPage, pageSize)
    }

    private fun getThinkingList(page: Int, size: Int) {
        BlogRetrofit.thinkingApi.getThinkingList(page, size)
            .enqueue(object : Callback<BaseResponse<BlogThinking>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogThinking>>,
                    response: Response<BaseResponse<BlogThinking>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.code == Constants.CODE_SUCCESS) {
                            val blogThinking = response.body()?.data
                            currentPage = blogThinking?.currentPage!!
                            noMore = blogThinking.noMore
                            pageSize = blogThinking.pageSize
                            total = blogThinking.total
                            thinkingList.clear()
                            blogThinking.data?.let { thinkingList.addAll(it) }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BlogThinking>>, error: Throwable) {
                    Log.d(TAG, "onFailure: $error")
                }

            })
    }
}