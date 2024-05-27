package top.manpok.blog.pojo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SettingItemData(
    @StringRes val name: Int,
    @DrawableRes val rightIcon: Int
)
