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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.manpok.blog.R
import top.manpok.blog.component.CommonHeader
import top.manpok.blog.ds.DataStoreManager
import top.manpok.blog.widget.CounterWidgetProvider

class CounterActivity : BaseActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initTitle = intent.getStringExtra(EXTRA_TITLE)
            ?: DataStoreManager.instance.getCounterTitleSync(this)
        val initCount = DataStoreManager.instance.getCounterCountSync(this)
        val initIncrement = DataStoreManager.instance.getCounterIncrementSync(this)
        setContent {
            var count by remember { mutableStateOf(initCount) }
            var title by remember { mutableStateOf(initTitle) }
            var increment by remember { mutableStateOf(initIncrement) }
            var showTitleDialog by remember { mutableStateOf(false) }
            var showIncrementDialog by remember { mutableStateOf(false) }
            var editText by remember { mutableStateOf("") }
            var editIncrementText by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()

            if (showTitleDialog) {
                AlertDialog(
                    onDismissRequest = { showTitleDialog = false },
                    title = { Text(stringResource(R.string.counter_set_title)) },
                    text = {
                        OutlinedTextField(
                            value = editText,
                            onValueChange = { editText = it },
                            singleLine = true,
                            placeholder = { Text(stringResource(R.string.counter_input_title_hint)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (editText.isNotBlank()) {
                                    title = editText
                                    scope.launch {
                                        DataStoreManager.instance.setCounterTitle(
                                            this@CounterActivity,
                                            editText
                                        )
                                        CounterWidgetProvider.refreshWidgets(this@CounterActivity)
                                    }
                                    showTitleDialog = false
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
                                    CounterWidgetProvider.refreshWidgets(this@CounterActivity)
                                }
                                showTitleDialog = false
                            }
                        }) {
                            Text(stringResource(R.string.counter_save))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTitleDialog = false }) {
                            Text(stringResource(R.string.counter_cancel))
                        }
                    }
                )
            }

            if (showIncrementDialog) {
                AlertDialog(
                    onDismissRequest = { showIncrementDialog = false },
                    title = { Text(stringResource(R.string.counter_set_increment)) },
                    text = {
                        OutlinedTextField(
                            value = editIncrementText,
                            onValueChange = { editIncrementText = it },
                            singleLine = true,
                            placeholder = { Text(stringResource(R.string.counter_input_increment_hint)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                val value = editIncrementText.toFloatOrNull()
                                if (value != null && value > 0f) {
                                    increment = value
                                    scope.launch {
                                        DataStoreManager.instance.setCounterIncrement(
                                            this@CounterActivity,
                                            value
                                        )
                                        CounterWidgetProvider.refreshWidgets(this@CounterActivity)
                                    }
                                    showIncrementDialog = false
                                }
                            })
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val value = editIncrementText.toFloatOrNull()
                            if (value != null && value > 0f) {
                                increment = value
                                scope.launch {
                                    DataStoreManager.instance.setCounterIncrement(
                                        this@CounterActivity,
                                        value
                                    )
                                    CounterWidgetProvider.refreshWidgets(this@CounterActivity)
                                }
                                showIncrementDialog = false
                            }
                        }) {
                            Text(stringResource(R.string.counter_save))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showIncrementDialog = false }) {
                            Text(stringResource(R.string.counter_cancel))
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
                    text = count.toString().removeSuffix(".0"),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_article_title),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .clickable {
                            count += increment
                            scope.launch {
                                DataStoreManager.instance.setCounterCount(
                                    this@CounterActivity,
                                    count
                                )
                                CounterWidgetProvider.refreshWidgets(this@CounterActivity)
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
                        text = stringResource(R.string.counter_set_increment),
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.blue_4285f4))
                            .clickable {
                                editIncrementText = increment.toString()
                                showIncrementDialog = true
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                    Text(
                        text = stringResource(R.string.counter_set_title),
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.blue_4285f4))
                            .clickable {
                                editText = title
                                showTitleDialog = true
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                    Text(
                        text = stringResource(R.string.counter_reset),
                        fontSize = 16.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colorResource(id = R.color.blue_4285f4))
                            .clickable {
                                count = 0f
                                scope.launch {
                                    DataStoreManager.instance.setCounterCount(
                                        this@CounterActivity,
                                        0f
                                    )
                                    CounterWidgetProvider.refreshWidgets(this@CounterActivity)
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}
