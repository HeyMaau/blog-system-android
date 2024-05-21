package top.manpok.blog.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.viewmodel.CategoryViewModel

@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    CommonHeader(
        leftIcon = R.drawable.ic_arrow_back,
        rightIcon = R.drawable.ic_more,
        leftIconClick = { /*TODO*/ },
        rightIconClick = { /*TODO*/ }) {
        /*Row {
            Text(text =)
            Icon(imageVector = , contentDescription = )
        }*/
    }
}