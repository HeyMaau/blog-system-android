package top.manpok.blog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.manpok.blog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomDialog(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    ModalBottomSheet(
        onDismissRequest = onDismiss, dragHandle = null,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 12.dp, bottom = 20.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(text = stringResource(id = R.string.share), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(1) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy_link),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.copy_share_link),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}