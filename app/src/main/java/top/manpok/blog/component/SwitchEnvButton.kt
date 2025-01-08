package top.manpok.blog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.manpok.blog.R
import top.manpok.blog.viewmodel.EnvViewModel

@Composable
fun SwitchEnvButton(modifier: Modifier = Modifier) {
    val envViewModel: EnvViewModel = viewModel()
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.choose_env_type),
            fontSize = 16.sp,
            color = colorResource(R.color.text_article_title)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.env_prod),
                fontSize = 16.sp,
                color = colorResource(R.color.text_article_title)
            )
            RadioButton(selected = envViewModel.currentEnv == 0,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colorResource(
                        id = R.color.blue_0185fa
                    )
                ),
                onClick = {
                    envViewModel.saveCurrentEnv(0)
                })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.env_dev),
                fontSize = 16.sp,
                color = colorResource(R.color.text_article_title)
            )
            RadioButton(selected = envViewModel.currentEnv == 1,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colorResource(
                        id = R.color.blue_0185fa
                    )
                ),
                onClick = {
                    envViewModel.saveCurrentEnv(1)
                })
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewSwitchEnvButton(modifier: Modifier = Modifier) {
    SwitchEnvButton(modifier = Modifier.fillMaxWidth())
}