package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val isContentEmpty by remember {
                derivedStateOf {
                    feedbackViewModel.content.value.isEmpty()
                }
            }
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .verticalScroll(state = rememberScrollState())
                ) {
                    FeedbackInputArea(
                        dataList = feedbackViewModel.itemList,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = { feedbackViewModel.submitFeedback() },
                        enabled = !feedbackViewModel.showSubmitProgress && !isContentEmpty,
                        colors = ButtonColors(
                            containerColor = colorResource(id = R.color.blue_4285f4),
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = colorResource(id = R.color.blue_aa4285f4)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(id = R.string.submit), fontSize = 16.sp)
                            if (feedbackViewModel.showSubmitProgress) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .align(Alignment.CenterEnd),
                                    strokeWidth = 2.dp,
                                    strokeCap = StrokeCap.Round,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}