package top.manpok.blog.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.page.CategoryPage
import top.manpok.blog.page.HomePage
import top.manpok.blog.viewmodel.BlogScaffoldViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogScaffold(
    modifier: Modifier = Modifier,
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel()
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BlogNavigationBar(
                blogScaffoldViewModel.mBottomBarItemList,
                blogScaffoldViewModel.selectedBottomItemIndex
            ) {
                blogScaffoldViewModel.selectedBottomItemIndex = it
            }
        }
    ) {
        when (blogScaffoldViewModel.selectedBottomItemIndex) {
            0 -> HomePage(
                modifier
                    .padding(it)
                    .background(Color.White)
            )

            1 -> CategoryPage(
                modifier = modifier
                    .padding(it)
                    .background(Color.White)
            )
        }
    }
}