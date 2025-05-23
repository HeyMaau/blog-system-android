package top.manpok.blog.activity

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.viewmodel.CommonWebViewViewModel
import top.manpok.blog.viewmodel.ShareViewModel

class CommonWebViewActivity : BaseActivity() {
    private val TAG = "CommonWebViewActivity"

    companion object {
        const val INTENT_KEY_URL: String = "intent_key_url"
    }

    val commonWebViewViewModel: CommonWebViewViewModel by viewModels()
    private val shareViewModel: ShareViewModel by viewModels()

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent?.getStringExtra(INTENT_KEY_URL) ?: Constants.BASE_URL

        setContent {
            val showProgressBar by remember {
                derivedStateOf {
                    commonWebViewViewModel.progress < 100
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.bg_white))
                    .statusBarsPadding()
            ) {
                CommonHeader(
                    title = commonWebViewViewModel.title,
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    closeIcon = R.drawable.ic_close,
                    leftIconClick = {
                        if (webView.canGoBack()) {
                            webView.goBack()
                        } else {
                            finish()
                        }
                    },
                    closeIconClick = {
                        finish()
                    },
                    onShareClick = {
                        shareViewModel.copyWebUrl(this@CommonWebViewActivity, webView.url ?: url)
                    },
                    modifier = Modifier
                        .zIndex(2f)
                        .padding(12.dp, 0.dp)
                )
                Box {
                    if (showProgressBar) {
                        LinearProgressIndicator(
                            color = colorResource(id = R.color.blue_4285f4),
                            trackColor = Color.Transparent,
                            progress = {
                                commonWebViewViewModel.progress.toFloat() / 100
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .zIndex(1f)
                        )
                    }
                    AndroidView(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(
                                start = 0.dp,
                                top = 10.dp,
                                end = 0.dp,
                                bottom = 0.dp
                            )
                            .imePadding(), factory = {
                            webView = WebView(it)
                            webView.settings.apply {
                                setSupportZoom(false)
                                builtInZoomControls = false
                                displayZoomControls = false
                                javaScriptEnabled = true

                                useWideViewPort = true
                                loadWithOverviewMode = true
                                domStorageEnabled = true

                                userAgentString = userAgentString.replace("; wv", "")
                            }
                            webView.apply {

                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )

                                webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(
                                        view: WebView?,
                                        request: WebResourceRequest?
                                    ): Boolean {
                                        view?.loadUrl(request?.url.toString())
                                        return true
                                    }
                                }
                                webChromeClient = object : WebChromeClient() {
                                    override fun onReceivedTitle(view: WebView?, title: String?) {
                                        if (title != null) {
                                            commonWebViewViewModel.title = title
                                        }
                                    }

                                    override fun onProgressChanged(
                                        view: WebView?,
                                        newProgress: Int
                                    ) {
                                        commonWebViewViewModel.progress = newProgress
                                    }

                                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                        LogUtil.i(
                                            TAG,
                                            "onConsoleMessage: ${consoleMessage.toString()}"
                                        )
                                        return super.onConsoleMessage(consoleMessage)
                                    }
                                }
                            }
                        }, update = {
                            it.loadUrl(url)
                        })
                }
            }
        }
    }
}