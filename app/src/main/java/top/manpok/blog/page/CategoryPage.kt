package top.manpok.blog.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CategoryInfoCard
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.CategoryViewModel

@Composable
fun CategoryPage(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    Column(modifier = modifier) {
        CommonHeader(
            leftIcon = R.drawable.ic_arrow_back,
            rightIcon = R.drawable.ic_more,
            leftIconClick = { /*TODO*/ },
            rightIconClick = { /*TODO*/ }) {
            Row(modifier = Modifier.padding(12.dp, 0.dp)) {
                Text(text = "全部分类")
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_drop_down),
                    contentDescription = null
                )
            }
        }
        if (categoryViewModel.categoryList.isNotEmpty()) {
            CategoryInfoCard(
                avatarUrl = Constants.BASE_IMAGE_URL + categoryViewModel.categoryList[0].cover,
                userName = categoryViewModel.categoryList[0].name!!,
                categoryName = categoryViewModel.categoryList[0].name!!,
                categoryDesc = categoryViewModel.categoryList[0].description!!,
                totalArticle = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp)
            )
        }
    }
}