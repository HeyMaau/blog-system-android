package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import top.manpok.blog.R
import top.manpok.blog.component.AudioPlayerControlPanel
import top.manpok.blog.viewmodel.AudioViewModel

class AudioPlayerActivity : BaseActivity() {

    private val TAG = "AudioPlayerActivity"

    private var backgroundColor by mutableIntStateOf(Color.White.toArgb())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val audioViewModel: AudioViewModel = viewModel()

            val playState = audioViewModel.playState.collectAsState()

            DisposableEffect(key1 = Unit) {
                onDispose {
                    audioViewModel.onDestroy()
                }
            }

            GetPalette(url = audioViewModel.currentAudioCover)
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(backgroundColor))
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    AsyncImage(
                        model = audioViewModel.currentAudioCover,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = audioViewModel.currentAudioName,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = audioViewModel.currentAudioArtist, color = Color.White)
                    }
                }
                Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                    AudioPlayerControlPanel(
                        value = 50f,
                        maxValue = 100f,
                        onValueChange = {},
                        onClickPlay = {
                            audioViewModel.playOrPauseAudio()
                        },
                        onClickNext = {
                            audioViewModel.playNext()
                        },
                        playState = playState.value,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun GetPalette(url: String) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .allowHardware(false)
                .build()
        )

        if (painter.state is AsyncImagePainter.State.Success) {
            val bitmap =
                (painter.state as AsyncImagePainter.State.Success).result.drawable.toBitmap()
            Palette.from(bitmap).generate {
                backgroundColor = it?.getDarkVibrantColor(Color.White.toArgb())!!
            }
        }
    }
}