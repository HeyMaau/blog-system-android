package top.manpok.blog.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.FriendLinkItem
import top.manpok.blog.viewmodel.FriendLinkViewModel

@Composable
fun ToolsPage(
    modifier: Modifier = Modifier,
    friendLinkViewModel: FriendLinkViewModel = viewModel()
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 0.dp,
                    top = 20.dp,
                    end = 0.dp,
                    10.dp
                )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_tools),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(id = R.string.all_tools),
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp, 0.dp)
            )
        }
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(friendLinkViewModel.friendLinkList) {
                FriendLinkItem(
                    logoUrl = it?.logo,
                    name = it?.name!!,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }
}