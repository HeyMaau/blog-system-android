package top.manpok.blog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@Composable
fun LaunchPage(modifier: Modifier = Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.logo_start),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 30.sp,
                color = colorResource(
                    id = R.color.blue_4285f4
                )
            )
        }

}