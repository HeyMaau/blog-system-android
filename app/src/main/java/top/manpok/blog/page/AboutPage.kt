package top.manpok.blog.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.component.SettingItem
import top.manpok.blog.component.SwitchEnvButton
import top.manpok.blog.utils.Constants
import top.manpok.blog.viewmodel.AboutPageViewModel
import top.manpok.blog.viewmodel.UpdateViewModel

@Composable
fun AboutPage(
    modifier: Modifier = Modifier,
    aboutPageViewModel: AboutPageViewModel = viewModel(),
    updateViewModel: UpdateViewModel = viewModel()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp, 0.dp)
    ) {
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            bitmap = ImageBitmap.imageResource(id = R.mipmap.logo_about),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 15.dp)
        )
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
                isLast = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp),
                click = settingItemData.click
            )
        }
        SettingItem(
            name = R.string.check_update,
            rightIcon = R.drawable.ic_arrow_forward,
            isLast = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp),
            click = {
                updateViewModel.checkUpdate(true)
            }
        )
        HorizontalDivider(thickness = 0.5.dp)
        SwitchEnvButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}