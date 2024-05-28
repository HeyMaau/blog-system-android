package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.component.FeedbackInputArea
import top.manpok.blog.viewmodel.FeedbackViewModel

class FeedbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val feedbackViewModel: FeedbackViewModel = viewModel()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(colorResource(id = R.color.gray_f2f3f5))
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(12.dp, 0.dp)
            ) {
                CommonHeader(
                    title = stringResource(id = R.string.contact_me),
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    leftIconClick = { finish() },
                    rightIconClick = { /*TODO*/ })
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_feedback),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                FeedbackInputArea(
                    dataList = feedbackViewModel.itemList,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}