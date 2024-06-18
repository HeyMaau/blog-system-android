package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.manpok.blog.component.BlogScaffold
import top.manpok.blog.component.LaunchPage
import top.manpok.blog.ui.theme.BlogSystemAndroidTheme

class MainActivity : ComponentActivity() {

    private var launching by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(2000)
            launching = false
        }
        setContent {
            if (launching) {
                LaunchPage()
            } else {
                BlogSystemAndroidTheme {
                    BlogScaffold()
                }
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