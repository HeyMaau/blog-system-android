package top.manpok.blog.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import top.manpok.blog.R
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.TempData
import top.manpok.blog.utils.ToastUtil

class ShareViewModel : ViewModel() {

    fun copyArticleShareLink(context: Context, id: String) {
        val link: String = if (TempData.currentEnv == Constants.ENV_PROD) {
            Constants.BASE_ARTICLE_SHARE_LINK + id
        } else {
            Constants.BASE_ARTICLE_SHARE_LINK_DEV + id
        }
        write2Clipboard(context, link)
    }

    fun copyWebUrl(context: Context, url: String) {
        write2Clipboard(context, url)
    }

    fun copyThinkingShareLink(context: Context, id: String) {
        val link: String = if (TempData.currentEnv == Constants.ENV_PROD) {
            Constants.BASE_THINKING_SHARE_LINK + id
        } else {
            Constants.BASE_THINKING_SHARE_LINK_DEV + id
        }
        write2Clipboard(context, link)
    }

    private fun write2Clipboard(context: Context, link: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(Constants.CLIPBOARD_TAG_ARTICLE_SHARE_LINK, link)
        clipboardManager.setPrimaryClip(clipData)
        ToastUtil.showShortToast(R.string.already_copy_to_clipboard)
    }
}