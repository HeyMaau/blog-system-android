package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import top.manpok.blog.R
import top.manpok.blog.component.AuthorInfoBanner
import top.manpok.blog.component.CommonHeader

class ArticleDetailActivity : ComponentActivity() {

    companion object {
        const val INTENT_KEY_ARTICLE_ID = "intent_key_article_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.statusBarsPadding()) {
                CommonHeader(
                    title = "测试标题",
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = R.drawable.ic_more,
                    leftIconClick = { /*TODO*/ },
                    rightIconClick = { /*TODO*/ })
                AuthorInfoBanner(
                    avatarUrl = "https://ts1.cn.mm.bing.net/th/id/R-C.b51d09e7b36090dc04e54de5fa7d989e?rik=QigJQWhXl6Z0Kg&riu=http%3a%2f%2fpic1.nipic.com%2f2009-02-25%2f200922520173452_2.jpg&ehk=WfbXwRrfc%2bwsfPtdUJ4vBvQe1zs7v5OkkD%2fDm2Rliqc%3d&risl=&pid=ImgRaw&r=0",
                    name = "manpok",
                    sign = "hhhhhhhh"
                )
            }
        }
    }
}