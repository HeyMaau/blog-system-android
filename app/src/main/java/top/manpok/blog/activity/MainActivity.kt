package top.manpok.blog.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.manpok.blog.BuildConfig
import top.manpok.blog.component.BlogScaffold
import top.manpok.blog.component.LaunchPage
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.ui.theme.BlogSystemAndroidTheme
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.TempData
import top.manpok.blog.viewmodel.ArticleViewModel
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.EnvViewModel

class MainActivity : BaseActivity() {

    private var launching by mutableStateOf(true)

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
            val audioViewModel: AudioViewModel = viewModel()
            if (launching) {
                LaunchPage()
            } else {
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