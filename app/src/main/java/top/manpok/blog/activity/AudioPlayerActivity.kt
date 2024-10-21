package top.manpok.blog.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AudioPlayListDialog
import top.manpok.blog.component.AudioPlayerControlPanel
import top.manpok.blog.service.AudioService
import top.manpok.blog.utils.LogUtil
import top.manpok.blog.utils.TempData
import top.manpok.blog.viewmodel.AudioViewModel
import top.manpok.blog.viewmodel.GlobalViewModelManager

class AudioPlayerActivity : BaseActivity() {

    private val TAG = "AudioPlayerActivity"

    val audioViewModel: AudioViewModel = GlobalViewModelManager.audioViewModel

    private var animatableBackgroundColor = Animatable(Color(audioViewModel.currentBackgroundColor))

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.isAppearanceLightStatusBars = false
        setContent {
            val playState = audioViewModel.playState.collectAsState()

            LaunchedEffect(audioViewModel.currentAudioCover) {
                audioViewModel.getCoverBitmapAndPalette(audioViewModel.currentAudioCover)
            }
            LaunchedEffect(key1 = audioViewModel.currentBackgroundColor) {
                animatableBackgroundColor.animateTo(
                    targetValue = Color(audioViewModel.currentBackgroundColor),
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
            LaunchedEffect(key1 = Unit) {
                audioViewModel.refreshUIEvent.collect {
                    startAudioService()
                }
            }

            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(animatableBackgroundColor.value)
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable(interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        finish()
                                    })
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
                    var showPlayListDialog by remember {
                        mutableStateOf(false)
                    }

                    AudioPlayerControlPanel(
                        value = audioViewModel.currentAudioPosition.toFloat(),
                        maxValue = audioViewModel.currentAudioDuration.toFloat(),
                        duration = audioViewModel.currentAudioDurationStr,
                        position = audioViewModel.currentAudioPositionStr,
                        playMode = audioViewModel.currentPlayMode,
                        onValueChange = {
                            audioViewModel.currentAudioPosition = it.toLong()
                        },
                        onValueChangeFinished = {
                            audioViewModel.seekTo(it.toLong())
                        },
                        onClickPlay = {
                            audioViewModel.playOrPauseAudio()
                        },
                        onClickNext = {
                            audioViewModel.playNext()
                        },
                        onClickPre = {
                            audioViewModel.playPre()
                        },
                        onPlayModeChange = {
                            audioViewModel.changePlayMode(it)
                        },
                        onPlayListClick = {
                            showPlayListDialog = true
                        },
                        playState = playState.value,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    if (showPlayListDialog) {
                        AudioPlayListDialog(
                            data = audioViewModel.audioList,
                            playMode = audioViewModel.currentPlayMode,
                            currentIndex = audioViewModel.currentIndex,
                            onPlayModeChange = {
                                audioViewModel.changePlayMode(it)
                            },
                            onItemClick = {
                                audioViewModel.seekToAudio(it)
                            },
                            onDismiss = {
                                showPlayListDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun startAudioService() {
        if (!TempData.hasNotificationPermission) {
            LogUtil.i(TAG, "No Notification Permission")
            return
        }
        val intent = Intent(this, AudioService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}