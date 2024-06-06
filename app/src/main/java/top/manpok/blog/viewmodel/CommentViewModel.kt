package top.manpok.blog.viewmodel

import android.text.TextUtils
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
import top.manpok.blog.pojo.BlogComment
import top.manpok.blog.utils.Constants

class CommentViewModel : ViewModel() {

    private val TAG = "CommentViewModel"

    val commentList = mutableStateListOf<BlogComment.Data?>()
    var currentPage by mutableIntStateOf(Constants.DEFAULT_PAGE)
    var noMore by mutableStateOf(true)
    var pageSize by mutableIntStateOf(Constants.DEFAULT_PAGE_SIZE)
    var total by mutableIntStateOf(0)

    fun getCommentList(page: Int, size: Int, type: Int, articleID: String?) {
        if (articleID == null || TextUtils.isEmpty(articleID)) {
            return
        }
        BlogRetrofit.commentApi.getCommentList(
            page = page,
            size = size,
            type = type,
            articleID = articleID
        )
            .enqueue(object : Callback<BaseResponse<BlogComment>> {
                override fun onResponse(
                    call: Call<BaseResponse<BlogComment>>,
                    response: Response<BaseResponse<BlogComment>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.code == Constants.CODE_SUCCESS) {
                            val blogComment = response.body()?.data
                            currentPage = blogComment?.currentPage!!
                            noMore = blogComment.noMore
                            pageSize = blogComment.pageSize
                            total = blogComment.total
                            if (currentPage == Constants.DEFAULT_PAGE) {
                                commentList.clear()
                            }
                            blogComment.data?.let { commentList.addAll(it) }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<BlogComment>>, error: Throwable) {
                    Log.d(TAG, "onFailure: $error")
                }

            })
    }

}