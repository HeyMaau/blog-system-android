package top.manpok.blog.webview

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import top.manpok.blog.utils.Constants

class BlogWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript("javascript:blogHighlightAll()", null)
        view?.evaluateJavascript("javascript:callAddImageOnClick()", null)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request != null) {
            val host = request.url?.host
            if (host != null && host.contains(Constants.HOST_NAME)) {
                return false
            }
            val intent = Intent()
            intent.setAction("android.intent.action.VIEW")
            intent.setData(request.url)
            view?.context?.startActivity(intent)
        }
        return true
    }
}