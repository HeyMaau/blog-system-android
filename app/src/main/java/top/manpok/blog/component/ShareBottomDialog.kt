package top.manpok.blog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.manpok.blog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomDialog(
    onDismiss: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = onDismiss, dragHandle = null,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = colorResource(R.color.bg_white),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(top = 12.dp, bottom = 32.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.share),
                fontSize = 16.sp,
                color = colorResource(R.color.text_article_title)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(1) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onItemClick.invoke()
                            coroutineScope.launch {
                                modalBottomSheetState.hide()
                                onDismiss()
                            }
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy_link),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.copy_share_link),
                            fontSize = 12.sp,
                            color = colorResource(R.color.text_article_title)
                        )
                    }
                }
            }
        }
    }
}