package top.manpok.blog.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.SettingItem
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.AboutPageViewModel

@Composable
fun AboutPage(modifier: Modifier = Modifier, aboutPageViewModel: AboutPageViewModel = viewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp, 0.dp)
    ) {
        val context = LocalContext.current
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.thinking_default),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(text = stringResource(id = R.string.app_name))
        val versionName =
            context.packageManager.getPackageInfo(Constants.PACKAGE_NAME, 0).versionName
        Text(
            text = stringResource(id = R.string.app_version, versionName),
            modifier = Modifier.padding(
                bottom = 30.dp
            )
        )
        HorizontalDivider(thickness = 0.5.dp)
        aboutPageViewModel.settingItemList.forEachIndexed { index, settingItemData ->
            SettingItem(
                name = settingItemData.name,
                rightIcon = settingItemData.rightIcon,
                isLast = index == aboutPageViewModel.settingItemList.size - 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            )
        }
        HorizontalDivider(thickness = 0.5.dp)
    }
}