package top.manpok.blog.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.viewmodel.ArticleDetailViewModel
import top.manpok.blog.webview.BlogWebChromeClient
import top.manpok.blog.webview.BlogWebViewClient

class ArticleDetailActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_ARTICLE_ID = "intent_key_article_id"
    }

    lateinit var viewModel: ArticleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(INTENT_KEY_ARTICLE_ID)
        viewModel = ArticleDetailViewModel(id)
        setContent {
            val rememberScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState)
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(12.dp)
            ) {
                CommonHeader(
                    title = viewModel.title,
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    leftIconClick = {
                        finish()
                    },
                    rightIconClick = { /*TODO*/ })
                Text(
                    text = viewModel.title,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(0.dp, 15.dp)
                )
                AuthorInfoBanner(
                    avatarUrl = viewModel.authorAvatar,
                    name = viewModel.authorName,
                    sign = viewModel.authorSign
                )
                if (!TextUtils.isEmpty(viewModel.cover)) {
                    AsyncImage(
                        model = viewModel.cover,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 12.dp)
                    )
                }
                if (!TextUtils.isEmpty(viewModel.content)) {
                    AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                        val webView = WebView(it)
                        webView.settings.apply {
                            setSupportZoom(false)
                            builtInZoomControls = false
                            displayZoomControls = false
                            useWideViewPort = true
                            javaScriptEnabled = true
                        }
                        webView.apply {
                            webView.webViewClient = BlogWebViewClient()
                            webChromeClient = BlogWebChromeClient()
                            loadDataWithBaseURL(
                                "file:///android_asset/",
                                viewModel.content,
                                "text/html",
                                "utf-8",
                                null
                            )
                        }
                    })
                }
                if (!TextUtils.isEmpty(viewModel.updateTime)) {
                    Text(
                        text = stringResource(id = R.string.update_time, viewModel.updateTime),
                        color = colorResource(id = R.color.gray_878789),
                        modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 30.dp)
                    )
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