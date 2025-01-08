package top.manpok.blog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R
import top.manpok.blog.pojo.DefaultState

@Composable
fun DefaultUIState(
    state: DefaultState,
    modifier: Modifier = Modifier,
    hint: String = "",
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(colorResource(R.color.bg_white))
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onClick?.invoke()
                })
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = if (state == DefaultState.NETWORK_ERROR) R.drawable.ic_network_error else R.drawable.logo_empty_search_result),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = hint,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 20.dp),
            color = colorResource(id = R.color.text_category_author_name)
        )
    }
}