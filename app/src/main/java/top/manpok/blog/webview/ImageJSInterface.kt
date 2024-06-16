package top.manpok.blog.webview

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import top.manpok.blog.activity.ImagePreviewActivity
import top.manpok.blog.viewmodel.ArticleDetailViewModel

class ImageJSInterface(
    private val context: Context,
    private val articleDetailViewModel: ArticleDetailViewModel
) {

    @JavascriptInterface
    fun openImagePreview(url: String) {
        val intent = Intent(
            context,
            ImagePreviewActivity::class.java
        )
        val imageList = articleDetailViewModel.imageList
        intent.putStringArrayListExtra(
            ImagePreviewActivity.INTENT_KEY_IMAGE_LIST,
            imageList as ArrayList<String>?
        )
        val index = articleDetailViewModel.imageMap[url]
        intent.putExtra(ImagePreviewActivity.INTENT_KEY_CURRENT_INDEX, index)
        context.startActivity(intent)
    }
}