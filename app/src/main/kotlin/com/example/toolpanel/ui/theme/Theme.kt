package com.example.toolpanel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// COLOR PALETTE
val OffWhite = Color(0xFFF7F7F7)
val DarkGray = Color(0xFF222222)

private val LightColorScheme = lightColorScheme(
    background = OffWhite,
    surface = OffWhite,
    onSurface = DarkGray,
    onBackground = DarkGray,
)

// TYPOGRAPHY
val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = DarkGray
    )
)

@Composable
fun ToolPanelTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
