package top.manpok.blog.viewmodel

import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAppInfo
import top.manpok.blog.utils.Constants
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs

class UpdateViewModel : ViewModel() {

    var showUpdateDialog: Boolean by mutableStateOf(false)

    init {
        if (shouldShowUpdateDialog()) {
            val packageManager = BaseApplication.getApplication().packageManager
            val packageInfo =
                packageManager.getPackageInfo(BaseApplication.getApplication().packageName, 0)
            BlogRetrofit.updateApi.checkUpdateInfo(packageInfo.longVersionCode.toInt())
                .enqueue(object : Callback<BaseResponse<BlogAppInfo>> {
                    override fun onResponse(
                        call: Call<BaseResponse<BlogAppInfo>>,
                        response: Response<BaseResponse<BlogAppInfo>>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.code == Constants.CODE_SUCCESS) {
                                val data = body.data
                                if (!TextUtils.isEmpty(data?.downloadUrl)) {
                                    showUpdateDialog = true
                                }
                            }
                        }
                    }

                    override fun onFailure(p0: Call<BaseResponse<BlogAppInfo>>, p1: Throwable) {

                    }

                })
        }
    }

    private fun shouldShowUpdateDialog(): Boolean {
        val lastCloseUpdateDialogTime =
            DataStoreManager.instance.getLastCloseUpdateDialogTimeSync(BaseApplication.getApplication())
        val lastCloseUpdateDialogTimeInstant = Instant.ofEpochMilli(lastCloseUpdateDialogTime)
        val lastCloseUpdateDialogTimeLocalDateTime =
            LocalDateTime.ofInstant(lastCloseUpdateDialogTimeInstant, ZoneId.systemDefault())
        val lastCloseUpdateDialogTimeDay = lastCloseUpdateDialogTimeLocalDateTime.dayOfYear

        val nowDay = LocalDateTime.now().dayOfYear
        return abs(nowDay - lastCloseUpdateDialogTimeDay) >= 3
    }
}