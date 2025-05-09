package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.pojo.BlogAudio
import top.manpok.blog.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayListDialog(
    data: List<BlogAudio.Data?>,
    modifier: Modifier = Modifier,
    playMode: Int = Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK,
    currentIndex: Int,
    onDismiss: () -> Unit,
    onPlayModeChange: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(10.dp),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        containerColor = colorResource(R.color.bg_white_dark_282828),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.bg_white_dark_282828))
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.current_play_list, data.size),
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = colorResource(R.color.text_article_title)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 12.dp, top = 12.dp, bottom = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            val tempPlayMode: Int = (playMode + 1) % 3
                            onPlayModeChange(tempPlayMode)
                        })
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (playMode == Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK) R.drawable.ic_sequential_playback else if (playMode == Constants.PLAY_MODE_REPEAT_MODE_ONE) R.drawable.ic_repeat_mode_one else R.drawable.ic_shuffle_playback),
                    contentDescription = null,
                    tint = colorResource(id = R.color.text_category_author_name),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = if (playMode == Constants.PLAY_MODE_SEQUENTIAL_PLAYBACK) R.string.list_loop else if (playMode == Constants.PLAY_MODE_REPEAT_MODE_ONE) R.string.repeat_one else R.string.shuffle_mode),
                    color = colorResource(id = R.color.text_category_author_name)
                )
            }
            LazyColumn {
                itemsIndexed(data) { index, item ->
                    Text(
                        buildAnnotatedString {
                            append(item?.name)
                            withStyle(
                                style = SpanStyle(
                                    color = if (currentIndex == index) colorResource(
                                        id = R.color.blue_0185fa
                                    ) else colorResource(id = R.color.text_category_author_name)
                                )
                            ) {
                                append(" - ${item?.artist}")
                            }
                        },
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (currentIndex == index) colorResource(id = R.color.blue_0185fa) else colorResource(
                            R.color.text_article_title
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    onItemClick(index)
                                    coroutineScope.launch {
                                        modalBottomSheetState.hide()
                                        onDismiss()
                                    }
                                }
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAudioPlayListDialog() {
    AudioPlayListDialog(
        data = listOf(),
        onDismiss = {},
        onPlayModeChange = {},
        currentIndex = 0,
        onItemClick = {})
}