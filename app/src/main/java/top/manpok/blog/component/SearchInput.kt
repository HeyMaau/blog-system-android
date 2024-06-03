package top.manpok.blog.component

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import top.manpok.blog.R

@Composable
fun SearchInput(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "",
    onTextChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        modifier = modifier,
        decorationBox = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(colorResource(id = R.color.gray_f8f8fa), shape = CircleShape)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = colorResource(id = R.color.blue_95b5e0),
                    modifier = Modifier.size(16.dp)
                )
                Box(modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(1f)) {
                    if (TextUtils.isEmpty(text) && !TextUtils.isEmpty(hint)) {
                        Text(text = hint, color = colorResource(id = R.color.gray_878789))
                    }
                    it()
                }
            }
        })
}