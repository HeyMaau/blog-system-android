package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.AsyncImage

class ImagePreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // set up all transformation states
                var scale by remember { mutableStateOf(1f) }
                val state =
                    rememberTransformableState { zoomChange, _, _ ->
                        val tempScale = scale * zoomChange
                        scale = if (tempScale <= 1) {
                            1f
                        } else {
                            tempScale
                        }
                    }
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        // add transformable to listen to multitouch transformation events
                        // after offset
                        .transformable(state = state)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = "https://ts1.cn.mm.bing.net/th/id/R-C.d3cd5a1b95416ae960efbc5e2d2ec702?rik=zU%2bcirCFjy0rYg&riu=http%3a%2f%2fy3.ifengimg.com%2fa%2f2014_52%2f2633f87e648cb10.jpg&ehk=DC4quUX3zSvpKlP6cWEl9xE3ehCJcpbRyAPs%2fBPY%2f3I%3d&risl=&pid=ImgRaw&r=0",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}