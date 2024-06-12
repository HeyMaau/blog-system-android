package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.utils.Constants
import top.manpok.blog.utils.Emoji
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditCommentBottomDialog(
    modifier: Modifier = Modifier,
    loading: Boolean,
    contentText: TextFieldValue,
    nicknameText: TextFieldValue,
    emailText: TextFieldValue,
    onContentTextChange: (TextFieldValue) -> Unit,
    onNicknameTextChange: (TextFieldValue) -> Unit,
    onEmailTextChange: (TextFieldValue) -> Unit,
    onEmojiClick: (String) -> Unit,
    onCommitClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    var showEmojiPanel by remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState {
        ceil(Emoji.list.size.toDouble() / Constants.EMOJI_NUM_PER_PAGE).toInt()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    CommentBasicTextField(
                        minLines = 2,
                        text = contentText,
                        hintText = R.string.welcome_to_congratulate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    ) {
                        onContentTextChange(it)
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .height(0.5.dp), color = colorResource(id = R.color.gray_cccccc)
                    ) {

                    }
                    CommentBasicTextField(
                        minLines = 2,
                        text = nicknameText,
                        hintText = R.string.enter_your_nickname,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        onNicknameTextChange(it)
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .height(0.5.dp), color = colorResource(id = R.color.gray_cccccc)
                    ) {

                    }
                    CommentBasicTextField(
                        minLines = 2,
                        text = emailText,
                        hintText = R.string.enter_your_email_to_generate_avatar,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        onEmailTextChange(it)
                    }
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_full_screen),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current
                Image(
                    imageVector = ImageVector.vectorResource(id = if (showEmojiPanel) R.drawable.ic_keyboard else R.drawable.ic_emoji_panel),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (showEmojiPanel) {
                                keyboardController?.show()
                                showEmojiPanel = false
                            } else {
                                keyboardController?.hide()
                                showEmojiPanel = true
                            }
                        }
                )
                Surface(
                    color = colorResource(id = R.color.gray_878789), modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                        .height(20.dp)
                        .width(1.dp)

                ) {
                }
                LazyRow {
                    items(5) {
                        Text(
                            text = Emoji.list[it],
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    onEmojiClick(Emoji.list[it])
                                },
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End, modifier = Modifier
                    .background(
                        colorResource(id = R.color.gray_f8f8fa)
                    )
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.blue_0185fa),
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = stringResource(id = R.string.commit),
                    color = if (TextUtils.isEmpty(contentText.text) || TextUtils.isEmpty(
                            nicknameText.text
                        ) || TextUtils.isEmpty(emailText.text) || loading
                    ) colorResource(
                        id = R.color.blue_444285f4
                    ) else colorResource(id = R.color.blue_4285f4),
                    modifier = Modifier.clickable(enabled = !loading, onClick = {
                        onCommitClick()
                    })
                )
            }
            if (showEmojiPanel) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HorizontalPager(state = pagerState) {
                        EmojiPanelPage(
                            data = Emoji.list.subList(
                                Constants.EMOJI_NUM_PER_PAGE * it,
                                if (it == pagerState.pageCount - 1) Emoji.list.size else Constants.EMOJI_NUM_PER_PAGE * (it + 1),
                            ),
                            onClick = {
                                onEmojiClick(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                        )
                    }
                    PagerIndicator(
                        total = pagerState.pageCount,
                        current = pagerState.currentPage
                    )
                }
            }
        }
    }
}