package top.manpok.blog.page

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    TabRow(selectedTabIndex = 0, containerColor = Color.Transparent) {
        Tab(selected = true, onClick = { /*TODO*/ }) {
            Text(text = "推荐")
        }
        Tab(selected = false, onClick = { /*TODO*/ }) {
            Text(text = "想法")
        }
    }
}