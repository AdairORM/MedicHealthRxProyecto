package com.example.medichealthrx.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnBlue,
    primaryContainer = LightBlue,
    onPrimaryContainer = BlueDark,
    secondary = BlueSecondary,
    onSecondary = OnBlue,
    secondaryContainer = BlueSecondaryDark,
    onSecondaryContainer = BlueDark,
    background = BackgroundBlue,
    onBackground = BlueDark,
    surface = SurfaceBlue,
    onSurface = BlueDark,
    error = ErrorRed,
    onError = OnBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    onPrimary = OnBlue,
    primaryContainer = BluePrimary,
    onPrimaryContainer = LightBlue,
    secondary = BlueSecondaryDark,
    onSecondary = OnBlue,
    secondaryContainer = BluePrimary,
    onSecondaryContainer = LightBlue,
    background = BlueDark,
    onBackground = OnBlue,
    surface = BlueDark,
    onSurface = OnBlue,
    error = ErrorRed,
    onError = OnBlue
)

@Composable
fun MedicHealthRxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Tipografía personalizada
        content = content
    )
}
