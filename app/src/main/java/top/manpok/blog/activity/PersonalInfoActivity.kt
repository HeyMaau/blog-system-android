package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoColumn
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.viewmodel.UserViewModel

class PersonalInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel: UserViewModel = viewModel()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(colorResource(id = R.color.gray_f2f3f5))
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(12.dp, 0.dp)
            ) {
                CommonHeader(
                    title = stringResource(id = R.string.about_me),
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    leftIconClick = { finish() },
                    rightIconClick = { /*TODO*/ })
                Spacer(modifier = Modifier.height(50.dp))
                AsyncImage(
                    model = userViewModel.authorAvatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.height(20.dp))
                AuthorInfoColumn(
                    roles = userViewModel.authorMajor,
                    github = userViewModel.authorGithub,
                    sign = userViewModel.authorSign,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .background(Color.White)
                        .padding(12.dp, 0.dp)
                )
            }
        }
    }
}