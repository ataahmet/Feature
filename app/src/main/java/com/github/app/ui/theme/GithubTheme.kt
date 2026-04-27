package com.github.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val YellowSea = Color(0xFFFEAC00)
private val RadicalRed = Color(0xFFFF3368)
private val Tundora = Color(0xFF4A4A4A)
private val White = Color(0xFFFFFFFF)
private val LightGray = Color(0xFFE0E0E0)

private val GithubColorScheme =
    lightColorScheme(
        primary = YellowSea,
        secondary = RadicalRed,
        background = White,
        surface = White,
        onPrimary = White,
        onSecondary = White,
        onBackground = Tundora,
        onSurface = Tundora,
        outline = LightGray,
    )

@Composable
fun GithubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GithubColorScheme,
        content = content,
    )
}
