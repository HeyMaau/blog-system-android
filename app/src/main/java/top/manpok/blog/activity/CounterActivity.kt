package top.manpok.blog.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.ds.DataStoreManager

class CounterActivity : BaseActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initTitle = intent.getStringExtra(EXTRA_TITLE)
            ?: DataStoreManager.instance.getCounterTitleSync(this)
        val initCount = DataStoreManager.instance.getCounterCountSync(this)
        setContent {
            var count by remember { mutableIntStateOf(initCount) }
            var title by remember { mutableStateOf(initTitle) }
            var showDialog by remember { mutableStateOf(false) }
            var editText by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("设置标题") },
                    text = {
                        OutlinedTextField(
                            value = editText,
                            onValueChange = { editText = it },
                            singleLine = true,
                            placeholder = { Text("请输入标题") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (editText.isNotBlank()) {
                                    title = editText
                                    scope.launch {
                                        DataStoreManager.instance.setCounterTitle(
                                            this@CounterActivity,
                                            editText
                                        )
                                    }
                                    showDialog = false
                                }
                            })
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (editText.isNotBlank()) {
                                title = editText
                                scope.launch {
                                    DataStoreManager.instance.setCounterTitle(
                                        this@CounterActivity,
                                        editText
                                    )
                                }
                                showDialog = false
                            }
                        }) {
                            Text("保存")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("取消")
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .background(colorResource(id = R.color.bg_f2f3f5))
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp)
            ) {
                CommonHeader(
                    title = title,
                    leftIcon = R.drawable.ic_arrow_back,
                    rightIcon = null,
                    leftIconClick = { finish() },
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Text(
                    text = "$count",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_article_title),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .clickable {
                            count++
                            scope.launch {
                                DataStoreManager.instance.setCounterCount(
                                    this@CounterActivity,
                                    count
                                )
                            }
                        }
                        .padding(48.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "设置标题",
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.blue_4285f4))
                            .clickable {
                                editText = title
                                showDialog = true
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                    Text(
                        text = "重置",
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.blue_4285f4))
                            .clickable {
                                count = 0
                                scope.launch {
                                    DataStoreManager.instance.setCounterCount(
                                        this@CounterActivity,
                                        0
                                    )
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}
