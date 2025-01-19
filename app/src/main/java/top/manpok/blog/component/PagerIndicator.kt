package top.manpok.blog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun PagerIndicator(modifier: Modifier = Modifier, total: Int, current: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 0 until total) {
            Surface(
                shape = CircleShape,
                color = if (current == i) colorResource(id = R.color.text_category_author_name) else colorResource(
                    id = R.color.bg_cccccc
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
            ) {}
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewPagerIndicator() {
    PagerIndicator(total = 3, current = 1)
}