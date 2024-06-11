package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.utils.Emoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommentBottomDialog(
    modifier: Modifier = Modifier,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onEmojiClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                BasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    minLines = 2,
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                    },
                    decorationBox = { innerTextField ->
                        Box {
                            if (TextUtils.isEmpty(text.text)) {
                                Text(
                                    text = stringResource(id = R.string.welcome_to_congratulate),
                                    color = colorResource(
                                        id = R.color.gray_878789
                                    )
                                )
                            }
                            innerTextField()
                        }
                    })
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
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_emoji_panel),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
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
                horizontalArrangement = Arrangement.End, modifier = Modifier
                    .background(
                        colorResource(id = R.color.gray_f8f8fa)
                    )
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(text = stringResource(id = R.string.commit))
            }
        }
    }
}