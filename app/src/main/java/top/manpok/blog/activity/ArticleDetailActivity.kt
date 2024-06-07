package top.manpok.blog.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommentInput
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.FloatingHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.ArticleDetailViewModel
import top.manpok.blog.viewmodel.CommentViewModel
import top.manpok.blog.webview.BlogWebChromeClient
import top.manpok.blog.webview.BlogWebViewClient

class ArticleDetailActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_ARTICLE_ID = "intent_key_article_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(12.dp, 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
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
                            modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 30.dp)
                        )
                    }
                    CommentInput()
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
                            .padding(0.dp, 20.dp)
                    )
                }
                if (articleDetailViewModel.loading) {
                    Column(
                        modifier = Modifier
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