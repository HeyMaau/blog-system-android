package top.manpok.blog.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RippleConfiguration
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
val NoRippleTheme =
    RippleConfiguration(color = Color.Unspecified, rippleAlpha = RippleAlpha(0f, 0f, 0f, 0f))