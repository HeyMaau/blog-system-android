package top.manpok.blog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import top.manpok.blog.R

@Composable
fun UpdateDialog(modifier: Modifier = Modifier) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.version_update_title))
                    androidx.compose.material.Text(text = "V1.1.1")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "1、更新了很多很多很多很多很多很多很多\n" +
                            "2、更新了很多很多很多很多很多很多很多\n" +
                            "3、更新了很多很多很多很多很多很多很多",
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(
                            id = R.color.blue_0185fa
                        )
                    ), modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.update_now), color = Color.White)
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    elevation = null
                ) {
                    Text(
                        text = stringResource(id = R.string.update_next_time),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun PreviewUpdateDialog() {
    UpdateDialog(modifier = Modifier.fillMaxWidth())
}