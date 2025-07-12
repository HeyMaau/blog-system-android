package top.manpok.blog.component

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import top.manpok.blog.R
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.viewmodel.ArticleDetailViewModel
import top.manpok.blog.webview.BlogWebChromeClient
import top.manpok.blog.webview.BlogWebViewClient
import top.manpok.blog.webview.ImageJSInterface

@Composable
fun MarkDownWebView(
    viewModel: ArticleDetailViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            setBackgroundColor(context.getColor(R.color.bg_white))
            isVerticalScrollBarEnabled = false
            webViewClient = BlogWebViewClient(viewModel)
            webChromeClient = BlogWebChromeClient()
            loadUrl("file:///android_asset/markdown_template.html")
            addJavascriptInterface(
                ImageJSInterface(
                    context,
                    viewModel
                ), "img_api"
            )

            settings.apply {
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
                javaScriptEnabled = true

                val packageManager =
                    BaseApplication.getApplication().packageManager
                val versionName =
                    packageManager.getPackageInfo(
                        BaseApplication.getApplication().packageName,
                        0
                    ).versionName
                userAgentString += " manpok_app/$versionName"
            }
        }
    }

    AndroidView(
        factory = { webView },
        onRelease = {
            it.removeJavascriptInterface("img_api")
            it.removeAllViews()
            it.destroy()
        },
        modifier = modifier
    )
}