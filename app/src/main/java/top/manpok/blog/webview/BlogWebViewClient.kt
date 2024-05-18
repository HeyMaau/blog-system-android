package top.manpok.blog.webview

import android.webkit.WebView
import android.webkit.WebViewClient

class BlogWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript("javascript:blogHighlightAll()", null)
    }
}