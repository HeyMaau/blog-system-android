package top.manpok.blog.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiPanelPage(modifier: Modifier = Modifier, data: List<String>, onClick: (String) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(8), modifier = modifier) {
        items(data) {
            Text(
                text = it,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .clickable {
                        onClick(it)
                    })
        }
    }
}