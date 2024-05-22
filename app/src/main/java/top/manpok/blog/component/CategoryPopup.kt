package top.manpok.blog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun CategoryPopup(show: Boolean, yOffset: Int = 0, dismissRequest: () -> Unit) {
    if (show) {
        Popup(
            offset = IntOffset(0, yOffset),
            onDismissRequest = dismissRequest,
            properties = PopupProperties(
                focusable = true
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
                    )
            ) {
                Text(text = "测试分类")
                Text(text = "测试分类")
                Text(text = "测试分类")
                Text(text = "测试分类")
                Text(text = "测试分类")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCategoryPopup() {
}