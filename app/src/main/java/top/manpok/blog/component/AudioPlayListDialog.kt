package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayListDialog(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.current_play_list, 10),
                fontSize = 20.sp,
                fontWeight = FontWeight(500)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_sequential_playback),
                    contentDescription = null,
                    tint = colorResource(id = R.color.gray_878789),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.list_loop),
                    color = colorResource(id = R.color.gray_878789)
                )
            }
            LazyColumn {
                items(5) {
                    Text(
                        buildAnnotatedString {
                            append("可惜你是双子座")
                            withStyle(style = SpanStyle(color = colorResource(id = R.color.gray_878789))) {
                                append(" - 无名")
                            }
                        },
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)

                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAudioPlayListDialog() {
    AudioPlayListDialog(onDismiss = {})
}