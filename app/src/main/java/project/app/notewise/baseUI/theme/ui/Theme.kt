package project.app.notewise.baseUI.theme.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = iconColor2Dark,
    onPrimary = textColorDark,
    primaryContainer = cardBackgroundDark,
    onPrimaryContainer = appBackgroundDark,
    inversePrimary = iconColor1Dark,
    secondary = badgeColorDark,
    onSecondary = Color.White,
    secondaryContainer = cardBackgroundDark,
    onSecondaryContainer = appBackgroundDark,
    tertiary = iconColor1Dark,
    onTertiary = textColorDark,
    tertiaryContainer = cardBackgroundDark,
    onTertiaryContainer = appBackgroundDark,
    background = appBackgroundDark,
    onBackground = textColorDark,
    surface = cardBackgroundDark,
    onSurface = textColorDark,
    surfaceVariant = cardBackgroundDark,
    onSurfaceVariant = iconColor1Dark,
    surfaceTint = iconColor2Dark,
    inverseSurface = textColorDark,
    inverseOnSurface = appBackgroundDark,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.2f),
    onErrorContainer = Color.Red,
    outline = iconColor1Dark,
    outlineVariant = iconColor2Dark,
    scrim = Color.Black.copy(alpha = 0.5f),
)


private val LightColorScheme = lightColorScheme(
    primary = iconColor2Light,
    onPrimary = textColorLight,
    primaryContainer = cardBackgroundLight,
    onPrimaryContainer = appBackgroundLight,
    inversePrimary = iconColor1Light,
    secondary = badgeColorLight,
    onSecondary = Color.White,
    secondaryContainer = cardBackgroundLight,
    onSecondaryContainer = appBackgroundLight,
    tertiary = iconColor1Light,
    onTertiary = textColorLight,
    tertiaryContainer = cardBackgroundLight,
    onTertiaryContainer = appBackgroundLight,
    background = appBackgroundLight,
    onBackground = textColorLight,
    surface = cardBackgroundLight,
    onSurface = textColorLight,
    surfaceVariant = cardBackgroundLight,
    onSurfaceVariant = iconColor1Light,
    surfaceTint = iconColor2Light,
    inverseSurface = textColorLight,
    inverseOnSurface = appBackgroundLight,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.2f),
    onErrorContainer = Color.Red,
    outline = iconColor1Light,
    outlineVariant = iconColor2Light,
    scrim = Color.Black.copy(alpha = 0.5f),
)


@Composable
fun NoteWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography,
        content = content
    )
}