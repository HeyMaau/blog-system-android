package top.manpok.blog.utils

import android.widget.Toast
import androidx.annotation.StringRes
import top.manpok.blog.base.BaseApplication

object ToastUtil {

    fun showShortToast(@StringRes text: Int) {
        Toast.makeText(BaseApplication.getApplication(), text, Toast.LENGTH_SHORT).show()
    }
}