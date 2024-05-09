package top.manpok.blog.page

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            divider = {},
            indicator = { tabPositions ->
                val currentTabPosition = tabPositions[selectedTabIndex]
                if (selectedTabIndex < tabPositions.size) {
                    val currentTabWidth by animateDpAsState(
                        targetValue = currentTabPosition.width / 4,
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                    )
                    val indicatorOffset by animateDpAsState(
                        targetValue = currentTabPosition.left + (currentTabPosition.width / 2 - currentTabWidth / 2),
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                    )
                    TabRowDefaults.Indicator(
                        color = colorResource(id = R.color.blue_4285f4),
                        height = 5.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.BottomStart)
                            .offset(x = indicatorOffset)
                            .width(currentTabWidth)

                    )
                }
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Tab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "推荐", modifier = Modifier.padding(5.dp))
            }
            Tab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "想法", modifier = Modifier.padding(5.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
        ) {

        }
    }

}