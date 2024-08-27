package top.manpok.blog.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.manpok.blog.R
import top.manpok.blog.activity.FeedbackActivity
import top.manpok.blog.activity.PersonalInfoActivity
import top.manpok.blog.pojo.SettingItemData
import top.manpok.blog.utils.LogUtil

class AboutPageViewModel : ViewModel() {

    val settingItemList = listOf(
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
            click = {
                val intent = Intent(it, FeedbackActivity::class.java)
                it.startActivity(intent)
            }
        ),
        SettingItemData(
            name = R.string.upload_log,
            rightIcon = R.drawable.ic_arrow_forward,
            click = {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        LogUtil.createZipFile()
                    }
                }
            }
        )
    )
}