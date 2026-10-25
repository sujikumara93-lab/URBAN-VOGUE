package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VogueBlack,
    onPrimary = VogueCream,
    primaryContainer = VogueGoldContainer,
    onPrimaryContainer = VogueGoldLight,
    secondary = VogueGold,
    onSecondary = VogueDeepBlack,
    secondaryContainer = VogueBeige,
    onSecondaryContainer = VogueBlack,
    tertiary = VogueGoldDark,
    onTertiary = Color.White,
    background = VogueCream,
    onBackground = VogueTextPrimary,
    surface = Color.White,
    onSurface = VogueTextPrimary,
    surfaceVariant = VogueCreamSurface,
    onSurfaceVariant = VogueTextSecondary,
    outline = VogueBorder,
    outlineVariant = VogueBeigeDark,
)

private val DarkColorScheme = darkColorScheme(
    primary = VogueGold,
    onPrimary = VogueDeepBlack,
    primaryContainer = VogueGoldContainer,
    onPrimaryContainer = VogueGoldLight,
    secondary = VogueGoldLight,
    onSecondary = VogueDeepBlack,
    secondaryContainer = VogueCharcoal,
    onSecondaryContainer = VogueCream,
    tertiary = VogueGoldLight,
    onTertiary = VogueDeepBlack,
    background = VogueDeepBlack,
    onBackground = VogueCream,
    surface = VogueBlack,
    onSurface = VogueCream,
    surfaceVariant = VogueBlackCard,
    onSurfaceVariant = VogueBeige,
    outline = VogueCharcoal,
    outlineVariant = VogueGoldDark,
)

@Composable
fun UrbanVogueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
