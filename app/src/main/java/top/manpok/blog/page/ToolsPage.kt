package top.manpok.blog.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.viewmodel.FriendLinkViewModel

@Composable
fun ToolsPage(
    modifier: Modifier = Modifier,
    friendLinkViewModel: FriendLinkViewModel = viewModel()
) {
    Text(text = "工具页")
}