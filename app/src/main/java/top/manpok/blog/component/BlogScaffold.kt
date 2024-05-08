package top.manpok.blog.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.page.HomePage
import top.manpok.blog.viewmodel.BlogScaffoldViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogScaffold(
    modifier: Modifier = Modifier,
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel()
) {
    Scaffold(
        bottomBar = {
            BlogNavigationBar(
                blogScaffoldViewModel.mBottomBarItemList,
                blogScaffoldViewModel.selectedBottomItemIndex.intValue
            ) {
                blogScaffoldViewModel.selectedBottomItemIndex.intValue = it
            }
        }
    ) {
        when (blogScaffoldViewModel.selectedBottomItemIndex.intValue) {
            0 -> HomePage(modifier.padding(it))
        }
    }
}