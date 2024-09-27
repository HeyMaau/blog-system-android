package top.manpok.blog.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.Window

object DensityUtil {

    /**
     * 根据手机的分辨率从 dp(相对大小) 的单位 转成为 px(像素)
     */
    fun dpToPx(context: Context, dpValue: Float): Int {
        // 获取屏幕密度
        val scale: Float = context.resources.displayMetrics.density
        // 结果+0.5是为了int取整时更接近
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp(相对大小)
     */
    fun pxToDp(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getStatusBarHeight(activity: Activity): Int {
        val rectangle = Rect()
        val window: Window = activity.window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }
}