package com.example.app_grupo7.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = AuraPrimary,
    onPrimary = AuraOnPrimary,
    primaryContainer = AuraPrimaryContainer,
    onPrimaryContainer = AuraOnPrimaryCont,
    secondary = AuraSecondary,
    onSecondary = AuraOnSecondary,
    secondaryContainer = AuraSecondaryCont,
    onSecondaryContainer = AuraOnSecondaryCont,
    tertiary = AuraTertiary,
    onTertiary = AuraOnTertiary,
    tertiaryContainer = AuraTertiaryCont,
    onTertiaryContainer = AuraOnTertiaryCont,
    background = AuraBackground,
    onBackground = AuraOnBackground,
    surface = AuraSurface,
    onSurface = AuraOnSurface,
    error = AuraError,
    onError = AuraOnError
)

private val DarkColors = darkColorScheme(
    primary = AuraPrimaryDark,
    onPrimary = AuraOnPrimaryDark,
    primaryContainer = AuraPrimaryContainerDark,
    onPrimaryContainer = AuraOnPrimaryContDark,
    secondary = AuraSecondaryDark,
    onSecondary = AuraOnSecondaryDark,
    secondaryContainer = AuraSecondaryContDark,
    onSecondaryContainer = AuraOnSecondaryContDark,
    tertiary = AuraTertiaryDark,
    onTertiary = AuraOnTertiaryDark,
    tertiaryContainer = AuraTertiaryContDark,
    onTertiaryContainer = AuraOnTertiaryContDark,
    background = AuraBackgroundDark,
    onBackground = AuraOnBackgroundDark,
    surface = AuraSurfaceDark,
    onSurface = AuraOnSurfaceDark,
    error = AuraError,
    onError = AuraOnErrorDark
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
