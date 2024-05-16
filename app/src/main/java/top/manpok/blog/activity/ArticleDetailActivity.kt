package top.manpok.blog.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.viewmodel.ArticleDetailViewModel

class ArticleDetailActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_ARTICLE_ID = "intent_key_article_id"
    }

    lateinit var viewModel: ArticleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(INTENT_KEY_ARTICLE_ID)
        viewModel = ArticleDetailViewModel(id)
        setContent {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(12.dp)
            ) {
                CommonHeader(
                    title = viewModel.title,
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    leftIconClick = {
                        finish()
                    },
                    rightIconClick = { /*TODO*/ })
                Text(
                    text = viewModel.title,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(0.dp, 15.dp)
                )
                AuthorInfoBanner(
                    avatarUrl = viewModel.authorAvatar,
                    name = viewModel.authorName,
                    sign = viewModel.authorSign
                )
                if (!TextUtils.isEmpty(viewModel.cover)) {
                    AsyncImage(
                        model = viewModel.cover,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 12.dp)
                    )
                }
            }
        }
    }
}