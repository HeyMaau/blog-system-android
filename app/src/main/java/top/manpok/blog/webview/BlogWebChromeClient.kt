package top.manpok.blog.webview

import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView

class BlogWebChromeClient : WebChromeClient() {
    private val TAG = "BlogWebChromeClient"

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        Log.d(TAG, "onReceivedTitle: $title")
    }
}