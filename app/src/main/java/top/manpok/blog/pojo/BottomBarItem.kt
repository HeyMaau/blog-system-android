package top.manpok.blog.pojo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomBarItem(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val label: Int
)
