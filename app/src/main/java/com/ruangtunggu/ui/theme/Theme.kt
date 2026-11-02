package com.ruangtunggu.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Material3 Color Schemes ────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark
)

// ── Custom Extra Colors ────────────────────────────────────────

@Immutable
data class RTExtraColors(
    val success: Color = Color.Unspecified,
    val warning: Color = Color.Unspecified,
    val queueActive: Color = Color.Unspecified,
    val queueBadgeBg: Color = Color.Unspecified
)

val LocalRTExtraColors = staticCompositionLocalOf { RTExtraColors() }

// ── Accessor ───────────────────────────────────────────────────

object RuangTungguTheme {
    /** Access custom colours that sit outside the Material3 palette. */
    val extraColors: RTExtraColors
        @Composable get() = LocalRTExtraColors.current
}

// ── Theme Composable ───────────────────────────────────────────

@Composable
fun RuangTungguTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val extraColors = if (darkTheme) {
        RTExtraColors(
            success = SuccessDark,
            warning = WarningDark,
            queueActive = QueueActiveDark,
            queueBadgeBg = QueueBadgeBgDark
        )
    } else {
        RTExtraColors(
            success = SuccessLight,
            warning = WarningLight,
            queueActive = QueueActiveLight,
            queueBadgeBg = QueueBadgeBgLight
        )
    }

    // Tint status bar
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalRTExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = RuangTungguTypography,
            shapes = RuangTungguShapes,
            content = content
        )
    }
}
