package top.manpok.blog.component

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R
import top.manpok.blog.pojo.BottomBarItem

@Composable
fun BlogNavigationBar(
    bottomBarItems: List<BottomBarItem>,
    selectedItemIndex: Int,
    onSelectedChanged: (index: Int) -> Unit
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Red,
            tonalElevation = 20.dp
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
                        indicatorColor = Color.White
                    )
                )
            }

            /*NavigationBarItem(selected = false, onClick = { *//*TODO*//* }, icon = {
                Icon(
                    imageVector = Icons.Rounded.Article,
                    contentDescription = null
                )
            }, label = {
                Text(text = stringResource(id = R.string.category_page))
            })
            NavigationBarItem(selected = false, onClick = { *//*TODO*//* }, icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_tools_ladder),
                    contentDescription = null
                )
            }, label = {
                Text(text = stringResource(id = R.string.tools_page))
            })
            NavigationBarItem(selected = false, onClick = { *//*TODO*//* }, icon = {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null
                )
            }, label = {
                Text(text = stringResource(id = R.string.about_page))
            })*/
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}