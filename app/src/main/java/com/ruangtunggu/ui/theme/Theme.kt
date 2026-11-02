package com.ruangtunggu.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RuangTungguLightColorScheme = lightColorScheme(
    primary = RTColors.BrandPrimaryTeal,
    onPrimary = RTColors.PureWhite,
    primaryContainer = RTColors.MintSurfaceActive,
    onPrimaryContainer = RTColors.BrandDarkTeal,
    secondary = RTColors.BrandLightTeal,
    onSecondary = RTColors.PureWhite,
    secondaryContainer = RTColors.QuickActionCariRsBg,
    onSecondaryContainer = RTColors.BrandPrimaryTeal,
    background = RTColors.ScreenBackground,
    onBackground = RTColors.NavyHeading,
    surface = RTColors.PureWhite,
    onSurface = RTColors.NavyHeading,
    surfaceVariant = RTColors.InputBackground,
    onSurfaceVariant = RTColors.SlateBodyText,
    outline = RTColors.InputBorder,
    error = RTColors.ErrorRed,
    onError = RTColors.PureWhite
)

@Suppress("FunctionName")
@Composable
fun RuangTungguTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = RTColors.BrandDarkTeal.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = RuangTungguLightColorScheme,
        typography = RuangTungguTypography,
        content = content
    )
}
