package top.manpok.blog.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.manpok.blog.BuildConfig
import top.manpok.blog.R
import top.manpok.blog.activity.FeedbackActivity
import top.manpok.blog.activity.PersonalInfoActivity
import top.manpok.blog.pojo.SettingItemData
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.ToastUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


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
                        LogUtil.flushLogBuffer()
                        LogUtil.createZipFile()
                        withContext(Dispatchers.Main) {
                            shareLog2Email(it)
                        }
                    }
                }
            }
        )
    )

    private fun shareLog2Email(context: Context) {
        val rootPath = context.getExternalFilesDir("log")
        val file = File(rootPath, "log.zip")
        if (file.exists()) {
            val uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("application/zip")
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            val recipients = arrayOf("841294180@163.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            val simpleDateFormat = SimpleDateFormat("yyyyMMddhhmmssSSS")
            val time = simpleDateFormat.format(Date())
            intent.putExtra(Intent.EXTRA_SUBJECT, "日志反馈-卧卷APP-$time")
            intent.putExtra(Intent.EXTRA_TEXT, "日志反馈-卧卷APP-$time")

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                ToastUtil.showShortToast(R.string.can_not_find_send_email_app)
            }
        } else {
            ToastUtil.showShortToast(R.string.empty_log_files)
        }
    }
}