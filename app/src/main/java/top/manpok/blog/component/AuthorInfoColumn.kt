package top.manpok.blog.component

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import top.manpok.blog.R
import top.manpok.blog.activity.CommonWebViewActivity

@Composable
fun AuthorInfoColumn(roles: String, github: String, sign: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        val context = LocalContext.current
        AuthorInfoColumnItem(text = roles, icon = R.drawable.ic_lang, rightIcon = null)
        AuthorInfoColumnItem(
            text = github,
            icon = R.drawable.ic_github,
            rightIcon = R.drawable.ic_arrow_forward,
            click = {
                val intent = Intent(context, CommonWebViewActivity::class.java)
                intent.putExtra(CommonWebViewActivity.INTENT_KEY_URL, github)
                context.startActivity(intent)
            }
        )
        AuthorInfoColumnItem(text = sign, icon = R.drawable.ic_sign, rightIcon = null)
    }
}