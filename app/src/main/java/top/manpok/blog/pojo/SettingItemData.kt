package top.manpok.blog.pojo

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingItemData(
    @StringRes val name: Int,
    @DrawableRes val rightIcon: Int,
    val click: (context: Context) -> Unit
)
