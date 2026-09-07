package com.whoanimal.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SageAccent,
    onPrimary = SageOnAccent,
    secondary = EmeraldSecondary,
    onSecondary = EmeraldOnSecondary,
    background = NatureBackgroundDark,
    surface = NatureSurfaceDark,
    onBackground = NatureOnBackgroundDark,
    onSurface = NatureOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = ForestGreenOnPrimary,
    secondary = EmeraldSecondary,
    onSecondary = EmeraldOnSecondary,
    tertiary = SageAccent,
    background = NatureBackgroundLight,
    surface = NatureSurfaceLight,
    onBackground = NatureOnBackgroundLight,
    onSurface = NatureOnSurfaceLight
)

@Composable
fun WhoAnimalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
