package top.manpok.blog.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.manpok.blog.BuildConfig
import top.manpok.blog.R
import top.manpok.blog.component.BlogScaffold
import top.manpok.blog.component.LaunchPage
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.ui.theme.BlogSystemAndroidTheme
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.TempData
import top.manpok.blog.utils.ToastUtil
import top.manpok.blog.viewmodel.ArticleViewModel
import top.manpok.blog.viewmodel.EnvViewModel

class MainActivity : BaseActivity() {

    private var launching by mutableStateOf(true)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                TempData.hasNotificationPermission = true
            } else {
                ToastUtil.showShortToast(R.string.notification_permission_for_audio_player)
                TempData.hasNotificationPermission = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            DataStoreManager.instance.getHasInitialized(this@MainActivity).collect {
                delay(1000)
                if (!it) {
                    delay(1000)
                    DataStoreManager.instance.setHasInitialized(this@MainActivity, true)
                }
                launching = false
            }
        }
        setContent {
            if (BuildConfig.DEBUG) {
                TempData.currentEnv = DataStoreManager.instance.getCurrentEnvSync(this)
                val envViewModel: EnvViewModel = viewModel()
            }
            val articleViewModel: ArticleViewModel = viewModel()
            if (launching) {
                LaunchPage()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkNotificationPermission()
                }
                BlogSystemAndroidTheme {
                    BlogScaffold()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch(Dispatchers.IO) {
            LogUtil.flushLogBuffer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                TempData.hasNotificationPermission = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                ToastUtil.showShortToast(R.string.notification_permission_for_audio_player)
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BlogSystemAndroidTheme {
        Greeting("Android")
    }
}