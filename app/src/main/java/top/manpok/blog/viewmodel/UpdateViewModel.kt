package top.manpok.blog.viewmodel

import android.os.Build
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.manpok.blog.api.BlogRetrofit
import top.manpok.blog.base.BaseApplication
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.pojo.BaseResponse
import top.manpok.blog.pojo.BlogAppInfo
import top.manpok.blog.utils.Constants
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.math.abs

class UpdateViewModel : ViewModel() {

    var showUpdateDialog: Boolean by mutableStateOf(false)
    var downloadUrl: String? = null
    var forceUpdate: Int by mutableIntStateOf(0)

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
}