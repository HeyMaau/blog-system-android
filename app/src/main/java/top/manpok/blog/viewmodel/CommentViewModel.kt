package top.manpok.blog.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogComment
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.ToastUtil

class CommentViewModel : ViewModel() {

    private val TAG = "CommentViewModel"

    private var _commitState: MutableStateFlow<CommitState> = MutableStateFlow(CommitState.Stop)
    val commitState = _commitState.asStateFlow()

    val commentList = mutableStateListOf<BlogComment.Data?>()
    var currentPage by mutableIntStateOf(Constants.DEFAULT_PAGE)
    var noMore by mutableStateOf(true)
    var pageSize by mutableIntStateOf(Constants.DEFAULT_PAGE_SIZE)
    var total by mutableIntStateOf(0)

    var contentInputState by mutableStateOf(
        TextFieldValue(text = "", TextRange(0))
    )
    var nicknameInputState by mutableStateOf(
        TextFieldValue(text = "", TextRange(0))
    )
    var emailInputState by mutableStateOf(
        TextFieldValue(text = "", TextRange(0))
    )

    var replyCommentId: String? = null
    var parentCommentId: String? = null
    var replyUserName: String? = null

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

    fun commitComment(articleId: String?, type: Int) {
        if (articleId == null) {
            return
        }
        _commitState.value = CommitState.Committing
        val data = BlogComment.Data(
            id = null,
            articleId = articleId,
            content = contentInputState.text,
            parentCommentId = parentCommentId,
            replyUserName = replyUserName,
            replyCommentId = replyCommentId,
            type = type,
            userEmail = emailInputState.text,
            userName = nicknameInputState.text,
            userAvatar = null,
            updateTime = null,
            children = null
        )
        if (TextUtils.isEmpty(replyCommentId)) {
            data.replyUserName = null
        }
        BlogRetrofit.commentApi.addComment(data).enqueue(object : Callback<BaseResponse<Unit>> {
            override fun onResponse(
                call: Call<BaseResponse<Unit>>,
                response: Response<BaseResponse<Unit>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.code == Constants.CODE_SUCCESS) {
                        ToastUtil.showShortToast(R.string.commit_comment_successfully)
                        getCommentList(
                            currentPage,
                            pageSize,
                            Constants.COMMENT_TYPE_ARTICLE,
                            articleId
                        )
                        _commitState.value = CommitState.Success
                        contentInputState = TextFieldValue(text = "", TextRange(0))
                        nicknameInputState = TextFieldValue(text = "", TextRange(0))
                        emailInputState = TextFieldValue(text = "", TextRange(0))
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<Unit>>, error: Throwable) {
                _commitState.value = CommitState.Error
                ToastUtil.showShortToast(R.string.commit_comment_fail)
            }
        })
    }

    fun updateCommitState(commitState: CommitState) {
        _commitState.value = commitState
    }

    sealed class CommitState {
        data object Stop : CommitState()
        data object Committing : CommitState()
        data object Success : CommitState()
        data object Error : CommitState()
    }
}