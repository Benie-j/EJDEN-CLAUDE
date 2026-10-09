package com.ejden.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palette reprise de la version web (primaire #176B87).
private val Primary = Color(0xFF176B87)
private val PrimarySoft = Color(0xFFE3F1F6)

private val Light = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimarySoft,
    onPrimaryContainer = Color(0xFF0B3A4A),
    background = Color(0xFFF6F8F9),
    surface = Color.White,
    error = Color(0xFFC0392B)
)

private val Dark = darkColorScheme(
    primary = Color(0xFF6FC3DD),
    onPrimary = Color(0xFF00323F),
    primaryContainer = Color(0xFF0F4A5E),
    onPrimaryContainer = Color(0xFFCDEEF8),
    background = Color(0xFF101417),
    surface = Color(0xFF171C20),
    error = Color(0xFFFF8A80)
)

@Composable
fun EjdenTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, content = content)
}
