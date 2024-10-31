package top.manpok.blog.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommentWindow
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.EditCommentBottomDialog
import top.manpok.blog.component.FloatingHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.CommentViewModel
import top.manpok.blog.viewmodel.ShareViewModel

class ThinkingDetailActivity : BaseActivity() {

    companion object {
        const val INTENT_KEY_THINKING_ID = "intent_key_thinking_id"
        const val INTENT_KEY_AUTHOR_NAME = "intent_key_author_name"
        const val INTENT_KEY_AUTHOR_SIGN = "intent_key_author_sign"
        const val INTENT_KEY_AUTHOR_AVATAR = "intent_key_author_avatar"
        const val INTENT_KEY_THINKING_TITLE = "intent_key_thinking_title"
        const val INTENT_KEY_THINKING_CONTENT = "intent_key_thinking_content"
        const val INTENT_KEY_THINKING_IMAGES = "intent_key_thinking_images"
        const val INTENT_KEY_THINKING_UPDATE_TIME = "intent_key_thinking_update_time"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val images = intent.getStringExtra(INTENT_KEY_THINKING_IMAGES)
        val id = intent.getStringExtra(INTENT_KEY_THINKING_ID)
        val splitImage = images?.split("-")
        setContent {
            val commentViewModel: CommentViewModel = viewModel()
            val shareViewModel: ShareViewModel = viewModel()
            var showCommentBottomDialog by remember {
                mutableStateOf(false)
            }
            var committingComment by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = Unit) {
                commentViewModel.commitState.collect {
                    when (it) {
                        CommentViewModel.CommitState.Success -> {
                            showCommentBottomDialog = false
                            committingComment = false
                        }

                        CommentViewModel.CommitState.Committing -> committingComment = true
                        CommentViewModel.CommitState.Error -> committingComment = false
                        else -> {}
                    }
                }
            }
            LaunchedEffect(key1 = Unit) {
                commentViewModel.getCommentList(
                    commentViewModel.currentPage,
                    commentViewModel.pageSize,
                    Constants.COMMENT_TYPE_THINKING,
                    id
                )
            }

            val scrollState = rememberScrollState()
            var commonHeaderHeight by remember {
                mutableIntStateOf(0)
            }
            val showFloatingHeader by remember {
                derivedStateOf {
                    scrollState.value > commonHeaderHeight * 2
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(12.dp, 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(state = scrollState)
                        .statusBarsPadding()
                ) {
                    CommonHeader(
                        title = intent.getStringExtra(INTENT_KEY_THINKING_TITLE),
                        leftIcon = R.drawable.ic_arrow_back,
                        rightIcon = R.drawable.ic_more,
                        leftIconClick = { finish() },
                        onShareClick = {
                            if (id != null) {
                                shareViewModel.copyThinkingShareLink(
                                    this@ThinkingDetailActivity,
                                    id
                                )
                            }
                        },
                        modifier = Modifier.onGloballyPositioned {
                            commonHeaderHeight = it.size.height
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                    AuthorInfoBanner(
                        avatarUrl = intent.getStringExtra(
                            INTENT_KEY_AUTHOR_AVATAR
                        ) ?: "",
                        name = intent.getStringExtra(INTENT_KEY_AUTHOR_NAME)!!,
                        sign = intent.getStringExtra(INTENT_KEY_AUTHOR_SIGN)!!
                    )
                    Text(
                        text = intent.getStringExtra(INTENT_KEY_THINKING_TITLE)!!,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(0.dp, 10.dp)
                    )
                    Text(
                        text = intent.getStringExtra(INTENT_KEY_THINKING_CONTENT)!!,
                        fontSize = 16.sp,
                        lineHeight = 1.5.em
                    )
                    if (splitImage?.size == 1) {
                        AsyncImage(
                            model = splitImage[0],
                            contentDescription = null,
                            modifier = Modifier
                                .padding(0.dp, 10.dp)
                                .clip(RoundedCornerShape(5))
                                .clickable {
                                    previewImage(splitImage, 0)
                                }
                        )
                    } else if (splitImage != null && splitImage.size >= 2) {
                        LazyRow(
                            modifier = Modifier.height(250.dp)
                        ) {
                            items(splitImage.size) {
                                if (it == 0) {
                                    AsyncImage(
                                        model = splitImage[it],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(0.dp, 10.dp, 5.dp, 10.dp)
                                            .clip(RoundedCornerShape(5))
                                            .clickable {
                                                previewImage(splitImage, it)
                                            }
                                    )
                                } else if (it != splitImage.size - 1) {
                                    AsyncImage(
                                        model = splitImage[it],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp, 10.dp)
                                            .clip(RoundedCornerShape(5))
                                            .clickable {
                                                previewImage(splitImage, it)
                                            }
                                    )
                                } else {
                                    AsyncImage(
                                        model = splitImage[it],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp, 10.dp, 0.dp, 10.dp)
                                            .clip(RoundedCornerShape(5))
                                            .clickable {
                                                previewImage(splitImage, it)
                                            }
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = stringResource(
                            id = R.string.update_time, intent.getStringExtra(
                                INTENT_KEY_THINKING_UPDATE_TIME
                            )!!
                        ),
                        color = colorResource(id = R.color.gray_878789),
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 30.dp)
                    )
                    CommentWindow(
                        commentTotal = commentViewModel.total,
                        commentList = commentViewModel.commentList,
                        onReplyClick = { parentCommentId, replyCommentId, replyUserName ->
                            if (replyUserName != null) {
                                commentViewModel.replyUserName = replyUserName
                            }
                            if (parentCommentId != null) {
                                commentViewModel.parentCommentId = parentCommentId
                            }
                            commentViewModel.replyCommentId = replyCommentId
                            if (parentCommentId == null && replyCommentId != null) {
                                commentViewModel.parentCommentId = replyCommentId
                                commentViewModel.replyCommentId = null
                            }
                            showCommentBottomDialog = true
                            commentViewModel.updateCommitState(CommentViewModel.CommitState.Stop)
                        }
                    ) {
                        showCommentBottomDialog = true
                        commentViewModel.clearReplyState()
                        commentViewModel.updateCommitState(CommentViewModel.CommitState.Stop)
                    }
                }
                if (showFloatingHeader) {
                    FloatingHeader(
                        leftIcon = R.drawable.ic_arrow_back,
                        rightIcon = R.drawable.ic_more,
                        leftIconClick = {
                            finish()
                        },
                        onShareClick = {
                            if (id != null) {
                                shareViewModel.copyThinkingShareLink(
                                    this@ThinkingDetailActivity,
                                    id
                                )
                            }
                        },
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(0.dp, 20.dp)
                    )
                }
            }

            if (showCommentBottomDialog) {
                val hintText: String = if (!TextUtils.isEmpty(commentViewModel.replyUserName)) {
                    stringResource(id = R.string.reply_to_who, commentViewModel.replyUserName!!)
                } else {
                    stringResource(id = R.string.welcome_to_congratulate)
                }
                EditCommentBottomDialog(
                    loading = committingComment,
                    contentHintText = hintText,
                    contentText = commentViewModel.contentInputState,
                    onContentTextChange = {
                        commentViewModel.contentInputState = it
                    },
                    nicknameText = commentViewModel.nicknameInputState,
                    onNicknameTextChange = {
                        commentViewModel.nicknameInputState = it
                    },
                    emailText = commentViewModel.emailInputState,
                    onEmailTextChange = {
                        commentViewModel.emailInputState = it
                    },
                    onEmojiClick = {
                        val result = commentViewModel.contentInputState.text.plus(it)
                        commentViewModel.contentInputState =
                            TextFieldValue(text = result, selection = TextRange(result.length))
                    },
                    onCommitClick = {
                        commentViewModel.commitComment(
                            articleId = id,
                            type = Constants.COMMENT_TYPE_THINKING
                        )
                    }) {
                    showCommentBottomDialog = false
                    commentViewModel.clearReplyState()
                }
            }
        }
    }

    private fun previewImage(imageList: List<String>, index: Int) {
        val intent = Intent(
            this,
            ImagePreviewActivity::class.java
        )
        if (imageList.size == 1) {
            intent.putStringArrayListExtra(
                ImagePreviewActivity.INTENT_KEY_IMAGE_LIST,
                arrayListOf(imageList[0])
            )
        } else {
            intent.putStringArrayListExtra(
                ImagePreviewActivity.INTENT_KEY_IMAGE_LIST,
                imageList as ArrayList<String>?
            )
        }
        intent.putExtra(ImagePreviewActivity.INTENT_KEY_CURRENT_INDEX, index)
        startActivity(intent)
    }
}