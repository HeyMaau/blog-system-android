package top.manpok.blog.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import okhttp3.internal.immutableListOf
import top.manpok.blog.R
import top.manpok.blog.activity.PersonalInfoActivity
import top.manpok.blog.pojo.SettingItemData

class AboutPageViewModel : ViewModel() {

    val settingItemList = immutableListOf(
        SettingItemData(
            name = R.string.about_me,
            rightIcon = R.drawable.ic_arrow_forward,
            click = {
                val intent = Intent(it, PersonalInfoActivity::class.java)
                it.startActivity(intent)
            }
        ),
        SettingItemData(
            name = R.string.contact_me,
            rightIcon = R.drawable.ic_arrow_forward,
            click = {}
        )
    )
}