package top.manpok.blog.component

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun FloatingHeader(modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = CircleShape)
                .clip(
                    CircleShape
                )
                .background(color = colorResource(id = R.color.white_f2ffffff))
                .padding(4.dp)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_more),
            contentDescription = null,
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = CircleShape)
                .clip(
                    CircleShape
                )
                .background(color = colorResource(id = R.color.white_f2ffffff))
                .padding(4.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PreviewFloatingHeader() {
    FloatingHeader()
}