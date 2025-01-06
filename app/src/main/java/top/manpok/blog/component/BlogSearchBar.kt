package top.manpok.blog.component

import android.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@Composable
fun BlogSearchBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                colorResource(id = R.color.blue_95b5e0),
                shape = RoundedCornerShape(50)
            )
            .padding(10.dp, 5.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            tint = colorResource(
                id = R.color.blue_95b5e0
            ),
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = stringResource(id = R.string.enter_key_word),
            color = colorResource(id = R.color.text_search_bar_hint),
            fontSize = 12.sp,
            modifier = Modifier.padding(5.dp, 0.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreviewBlogSearchBar() {
    BlogSearchBar()
}