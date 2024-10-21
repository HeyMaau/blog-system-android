package top.manpok.blog.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.R
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAppInfo
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.ToastUtil
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.math.abs

class UpdateViewModel : ViewModel() {

    var showUpdateDialog: Boolean by mutableStateOf(false)
    var downloadUrl: String? = null
    var forceUpdate: Int by mutableIntStateOf(0)
    var versionName: String? by mutableStateOf(null)
    var changeLog: String? by mutableStateOf(null)

    init {
        checkUpdate(false)
    }

    fun checkUpdate(checkManually: Boolean) {
        if (checkManually || shouldShowUpdateDialog()) {
            val packageManager = BaseApplication.getApplication().packageManager
            val packageInfo =
                packageManager.getPackageInfo(BaseApplication.getApplication().packageName, 0)
            val versionCode: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toInt()
            } else {
                packageInfo.versionCode
            }
            BlogRetrofit.updateApi.checkUpdateInfo(versionCode)
                .enqueue(object : Callback<BaseResponse<BlogAppInfo>> {
                    override fun onResponse(
                        call: Call<BaseResponse<BlogAppInfo>>,
                        response: Response<BaseResponse<BlogAppInfo>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.code == Constants.CODE_SUCCESS) {
                                val data = body.data
                                if (data != null && !TextUtils.isEmpty(data.downloadUrl)) {
                                    showUpdateDialog = true
                                    downloadUrl = data.downloadUrl
                                    forceUpdate = data.forceUpdate
                                    versionName = data.versionName
                                    changeLog = data.changeLog
                                    sendNotification()
                                } else {
                                    if (checkManually) {
                                        ToastUtil.showShortToast(R.string.already_latest_version)
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(p0: Call<BaseResponse<BlogAppInfo>>, p1: Throwable) {

                    }

                })
        }
    }

    fun handleCloseUpdateDialog() {
        val day: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().dayOfYear
        } else {
            Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        }
        viewModelScope.launch {
            DataStoreManager.instance.setLastCloseUpdateDialogTime(
                BaseApplication.getApplication(),
                day
            )

        }
    }

    private fun shouldShowUpdateDialog(): Boolean {
        val lastCloseUpdateDialogTime =
            DataStoreManager.instance.getLastCloseUpdateDialogTimeSync(BaseApplication.getApplication())
        val nowDay: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().dayOfYear
        } else {
            Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        }
        return abs(nowDay - lastCloseUpdateDialogTime) >= 3
    }

    private fun sendNotification() {
        val application = BaseApplication.getApplication()
        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder
        val uri = Uri.parse(downloadUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val pendingIntent = PendingIntent.getActivity(
            application.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID_NORMAL,
                application.getString(R.string.notification_channel_normal),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    application.getString(R.string.notification_channel_normal_description)
            }

            notificationManager.createNotificationChannel(channel)

            builder =
                NotificationCompat.Builder(application, Constants.NOTIFICATION_CHANNEL_ID_NORMAL)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(application.getString(R.string.notification_title_has_update))
                    .setContentText(application.getString(R.string.notification_content_text_click_to_update))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
        } else {
            builder =
                NotificationCompat.Builder(application)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(application.getString(R.string.notification_title_has_update))
                    .setContentText(application.getString(R.string.notification_content_text_click_to_update))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

        }
        notificationManager.notify(Constants.NOTIFICATION_ID_NORMAL, builder.build())
    }
}