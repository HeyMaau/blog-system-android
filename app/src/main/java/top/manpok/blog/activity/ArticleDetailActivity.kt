package top.manpok.blog.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommentWindow
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.EditCommentBottomDialog
import top.manpok.blog.component.FloatingHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.ArticleDetailViewModel
import top.manpok.blog.viewmodel.CommentViewModel
import top.manpok.blog.webview.BlogWebChromeClient
import top.manpok.blog.webview.BlogWebViewClient
import top.manpok.blog.webview.ImageJSInterface

class ArticleDetailActivity : BaseActivity() {

    companion object {
        const val INTENT_KEY_ARTICLE_ID = "intent_key_article_id"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(INTENT_KEY_ARTICLE_ID)
        setContent {
            val articleDetailViewModel: ArticleDetailViewModel = viewModel()
            val commentViewModel: CommentViewModel = viewModel()
            LaunchedEffect(key1 = Unit) {
                articleDetailViewModel.getFromDB(id)
                articleDetailViewModel.getArticleDetail(id)
                commentViewModel.getCommentList(
                    commentViewModel.currentPage,
                    commentViewModel.pageSize,
                    Constants.COMMENT_TYPE_ARTICLE,
                    id
                )
            }

            val scrollState = rememberScrollState()
            var commonHeaderHeight by remember {
                mutableIntStateOf(0)
            }
            val density = LocalDensity.current
            var commonHeaderHeightDP by remember {
                mutableStateOf(0.dp)
            }
            val showFloatingHeader by remember {
                derivedStateOf {
                    scrollState.value > commonHeaderHeight * 2
                }
            }

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

            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(12.dp, 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .statusBarsPadding()
                ) {
                    CommonHeader(
                        title = articleDetailViewModel.title,
                        leftIcon = R.drawable.ic_arrow_back,
                        rightIcon = R.drawable.ic_more,
                        leftIconClick = {
                            finish()
                        },
                        rightIconClick = { /*TODO*/ },
                        modifier = Modifier
                            .zIndex(1f)
                            .onGloballyPositioned {
                                commonHeaderHeight = it.size.height
                                with(density) {
                                    commonHeaderHeightDP = it.size.height.toDp()
                                }
                            })
                    Text(
                        text = articleDetailViewModel.title,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(0.dp, 15.dp)
                    )
                    AuthorInfoBanner(
                        avatarUrl = articleDetailViewModel.authorAvatar,
                        name = articleDetailViewModel.authorName,
                        sign = articleDetailViewModel.authorSign
                    )
                    if (!TextUtils.isEmpty(articleDetailViewModel.cover)) {
                        AsyncImage(
                            model = articleDetailViewModel.cover,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 12.dp)
                        )
                    }
                    if (!TextUtils.isEmpty(articleDetailViewModel.content)) {
                        AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                            val webView = WebView(it)
                            webView.settings.apply {
                                setSupportZoom(false)
                                builtInZoomControls = false
                                displayZoomControls = false
                                javaScriptEnabled = true
                            }
                            webView.apply {
                                webView.webViewClient = BlogWebViewClient()
                                webChromeClient = BlogWebChromeClient()
                                loadDataWithBaseURL(
                                    "file:///android_asset/",
                                    articleDetailViewModel.content,
                                    "text/html",
                                    "utf-8",
                                    null
                                )
                                addJavascriptInterface(
                                    ImageJSInterface(
                                        this@ArticleDetailActivity,
                                        articleDetailViewModel
                                    ), "img_api"
                                )
                            }
                        })
                    }
                    if (!TextUtils.isEmpty(articleDetailViewModel.updateTime)) {
                        Text(
                            text = stringResource(
                                id = R.string.update_time,
                                articleDetailViewModel.updateTime
                            ),
                            color = colorResource(id = R.color.gray_878789),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
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
                        rightIconClick = {},
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(0.dp, 20.dp)
                    )
                }
                if (articleDetailViewModel.loading) {
                    Column(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(top = commonHeaderHeightDP)
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        Surface(
                            color = colorResource(id = R.color.gray_cccccc),
                            shape = RoundedCornerShape(40),
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .height(15.dp)
                                .fillMaxWidth()
                        ) {}
                        AuthorInfoBanner(avatarUrl = "", name = "", sign = "", showSkeleton = true)
                        Spacer(modifier = Modifier.height(30.dp))
                        for (i in 0..3) {
                            Surface(
                                color = colorResource(id = R.color.gray_cccccc),
                                shape = RoundedCornerShape(30),
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .height(10.dp)
                                    .fillMaxWidth()
                            ) {}
                        }
                    }
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
                            type = Constants.COMMENT_TYPE_ARTICLE
                        )
                    }) {
                    showCommentBottomDialog = false
                    commentViewModel.clearReplyState()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}