package top.manpok.blog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import top.manpok.blog.R

@Composable
fun UpdateDialog(
    versionName: String?,
    changeLog: String?,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    forceUpdate: Boolean = false
) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_update),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.version_update_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (versionName != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = versionName)
                    }
                }
                if (changeLog != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = changeLog.replace("\\n", "\n"),
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onConfirmClick.invoke() }, colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(
                            id = R.color.blue_0185fa
                        )
                    ), modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.update_now), color = Color.White)
                }
                if (!forceUpdate) {
                    Button(
                        onClick = { onCancelClick.invoke() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = null
                    ) {
                        Text(
                            text = stringResource(id = R.string.update_next_time),
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun PreviewUpdateDialog() {
    UpdateDialog(
        versionName = "V1.1.1",
        changeLog = "1、更新了很多很多很多很多很多很多很多\n" +
                "2、更新了很多很多很多很多很多很多很多\n" +
                "3、更新了很多很多很多很多很多很多很多",
        onConfirmClick = {},
        onCancelClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}