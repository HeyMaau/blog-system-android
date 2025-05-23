package top.manpok.blog.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R
import top.manpok.blog.pojo.BottomBarItem
import top.manpok.blog.ui.theme.NoRippleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogNavigationBar(
    bottomBarItems: List<BottomBarItem>,
    selectedItemIndex: Int,
    onSelectedChanged: (index: Int) -> Unit
) {
    CompositionLocalProvider(LocalRippleConfiguration provides NoRippleTheme) {
        NavigationBar(
            containerColor = colorResource(R.color.bg_white),
            modifier = Modifier.shadow(elevation = 4.dp)
        ) {
            bottomBarItems.forEachIndexed { index, bottomBarItem ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = { onSelectedChanged(index) },
                    icon = {
                        Icon(
                            imageVector = if (selectedItemIndex == index) ImageVector.vectorResource(
                                bottomBarItem.selectedIcon
                            ) else ImageVector.vectorResource(bottomBarItem.unselectedIcon),
                            contentDescription = stringResource(id = bottomBarItem.label)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = bottomBarItem.label))
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.blue_4285f4),
                        selectedTextColor = colorResource(id = R.color.blue_4285f4),
                        indicatorColor = colorResource(R.color.bg_white)
                    )
                )
            }
        }
    }
}