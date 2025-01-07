package top.manpok.blog.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.page.AboutPage
import top.manpok.blog.page.CategoryPage
import top.manpok.blog.page.HomePage
import top.manpok.blog.page.ToolsPage
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.BlogScaffoldViewModel
import top.manpok.blog.viewmodel.UpdateViewModel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogScaffold(
    modifier: Modifier = Modifier,
    blogScaffoldViewModel: BlogScaffoldViewModel = viewModel(),
    updateViewModel: UpdateViewModel = viewModel()
) {
    Box(contentAlignment = Alignment.Center) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 }
        )
        Scaffold(
            containerColor = colorResource(R.color.bg_white),
            bottomBar = {
                val context = LocalContext.current
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                BlogNavigationBar(
                    blogScaffoldViewModel.mBottomBarItemList,
                    blogScaffoldViewModel.selectedBottomItemIndex
                ) {
                    vibrator.cancel()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val effect =
                            VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)
                        vibrator.vibrate(effect)
                    } else {
                        vibrator.vibrate(10)
                    }
                    if (blogScaffoldViewModel.selectedBottomItemIndex == it) {
                        blogScaffoldViewModel.dispatchEvent(
                            BlogScaffoldViewModel.ScaffoldIntent.SameBottomItemClick(
                                it
                            )
                        )
                    }
                    blogScaffoldViewModel.selectedBottomItemIndex = it
                }
            }
        ) {
            when (blogScaffoldViewModel.selectedBottomItemIndex) {
                0 -> HomePage(
                    modifier = modifier
                        .padding(it)
                        .background(colorResource(R.color.bg_white)),
                    pagerState = pagerState
                )

                1 -> CategoryPage(
                    modifier = modifier
                        .padding(it)
                        .background(colorResource(R.color.bg_white))
                )

                2 -> ToolsPage(
                    modifier = modifier
                        .padding(it)
                        .background(Color.White)
                )

                3 -> AboutPage(
                    modifier = Modifier
                        .padding(it)
                        .background(Color.White)
                )
            }
        }
        if (updateViewModel.showUpdateDialog) {
            val context = LocalContext.current
            UpdateDialog(
                versionName = updateViewModel.versionName,
                changeLog = updateViewModel.changeLog,
                onConfirmClick = {
                    if (updateViewModel.forceUpdate != Constants.FORCE_UPDATE_TRUE) {
                        updateViewModel.showUpdateDialog = false
                    }
                    if (!TextUtils.isEmpty(updateViewModel.downloadUrl)) {
                        val uri = Uri.parse(updateViewModel.downloadUrl)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                },
                onCancelClick = {
                    updateViewModel.showUpdateDialog = false
                    updateViewModel.handleCloseUpdateDialog()
                },
                forceUpdate = updateViewModel.forceUpdate == Constants.FORCE_UPDATE_TRUE
            )
        }
    }
}