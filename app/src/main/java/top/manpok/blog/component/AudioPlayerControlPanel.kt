package top.manpok.blog.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.AudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerControlPanel(
    value: Float,
    maxValue: Float,
    duration: String,
    position: String,
    modifier: Modifier = Modifier,
    playMode: Int = Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (Float) -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit,
    onClickPre: () -> Unit,
    onPlayModeChange: (Int) -> Unit,
    onPlayListClick: () -> Unit,
    playState: AudioViewModel.PlayState
) {
    var progress by remember {
        mutableFloatStateOf(0F)
    }
    var changing by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Slider(
                value = if (!changing) value else progress,
                onValueChange = {
                    changing = true
                    onValueChange(it)
                    progress = it
                },
                onValueChangeFinished = {
                    onValueChangeFinished(progress)
                    changing = false
                },
                valueRange = 0f..maxValue,
                thumb = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_slider_dot),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                track = {
                    SliderDefaults.Track(
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.White,
                            thumbColor = Color.White
                        ), enabled = true, sliderState = it,
                        thumbTrackGapSize = 0.dp,
                        drawStopIndicator = null,
                        modifier = Modifier.height(4.dp)
                    )
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(text = position, color = Color.White, fontSize = 12.sp)
            Text(text = duration, color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = if (playMode == Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK) R.drawable.ic_sequential_playback else if (playMode == Constants.PLAY_MODE_REPEAT_MODE_ONE) R.drawable.ic_repeat_mode_one else R.drawable.ic_shuffle_playback),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            val tempPlayMode: Int = (playMode + 1) % 3
                            onPlayModeChange(tempPlayMode)
                        })
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_previous),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClickPre
                    )
            )
            if (playState == AudioViewModel.PlayState.PreParing) {
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val rotateDegrees by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = ""
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_loading),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotateDegrees)
                )
            } else {
                Icon(
                    imageVector = if (playState != AudioViewModel.PlayState.Playing) ImageVector.vectorResource(
                        id = R.drawable.ic_play
                    ) else ImageVector.vectorResource(id = R.drawable.ic_pause),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onClickPlay() })
                )
            }
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClickNext
                    )
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_play_list),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            onPlayListClick()
                        })
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAudioPlayerControlPanel() {
    AudioPlayerControlPanel(
        value = 50f,
        maxValue = 100f,
        duration = "11:11",
        position = "00:00",
        onValueChange = {},
        onClickPlay = {},
        playState = AudioViewModel.PlayState.Stop,
        onClickNext = {},
        onClickPre = {},
        onPlayListClick = {},
        onPlayModeChange = {},
        onValueChangeFinished = {}
    )
}