package top.manpok.blog.page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.activity.ThinkingDetailActivity
import top.manpok.blog.component.ThinkingListItem
import top.manpok.blog.viewmodel.ThinkingViewModel

@Composable
fun HomeThinkingListPage(
    modifier: Modifier = Modifier,
    thinkingViewModel: ThinkingViewModel = viewModel()
) {
    val context = LocalContext.current
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(thinkingViewModel.thinkingList.toList(), key = {
            it.id!!
        }) {
            ThinkingListItem(data = it, click = {
                val intent = Intent(context, ThinkingDetailActivity::class.java)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_AUTHOR_NAME, it.user?.userName)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_AUTHOR_SIGN, it.user?.sign)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_AUTHOR_AVATAR, it.user?.avatar)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_TITLE, it.title)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_CONTENT, it.content)
                intent.putExtra(ThinkingDetailActivity.INTENT_KEY_THINKING_IMAGES, it.images)
                intent.putExtra(
                    ThinkingDetailActivity.INTENT_KEY_THINKING_UPDATE_TIME,
                    it.updateTime
                )
                context.startActivity(intent)
            })
        }
    }
}