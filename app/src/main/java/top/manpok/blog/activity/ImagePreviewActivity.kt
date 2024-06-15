package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import top.manpok.blog.R
import top.manpok.blog.utils.Constants


class ImagePreviewActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_IMAGE_LIST = "intent_key_image_list"
        const val INTENT_KEY_CURRENT_INDEX = "intent_key_current_index"
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val windowInsetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsControllerCompat.isAppearanceLightStatusBars = false
        super.onCreate(savedInstanceState)
        val imageList = intent?.getStringArrayListExtra(INTENT_KEY_IMAGE_LIST)
        val currentIndex = intent?.getIntExtra(INTENT_KEY_CURRENT_INDEX, 0)
        setContent {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            ) {
                if (imageList != null) {
                    val pagerState = rememberPagerState(initialPage = currentIndex ?: 0) {
                        imageList.size
                    }
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = Constants.BASE_IMAGE_URL + imageList[it],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(zoomState = rememberZoomState())
                        )
                    }
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 12.dp, top = 4.dp)
                        .clickable {
                            finish()
                        }
                )
            }
        }
    }
}