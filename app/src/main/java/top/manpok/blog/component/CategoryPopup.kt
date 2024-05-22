package top.manpok.blog.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun CategoryPopup(
    show: Boolean,
    yOffset: Int = 0,
    dismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (show) {
        Popup(
            offset = IntOffset(0, yOffset),
            onDismissRequest = dismissRequest,
            properties = PopupProperties(
                focusable = true
            )
        ) {
            content()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCategoryPopup() {
}