package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val MidnightDb = Color(0xFF0F0F13)
val DarkCarbon = Color(0xFF16161C)
val SoftCarbon = Color(0xFF22222B)
val CyberCyan = Color(0xFF00E5FF)
val NeonEmerald = Color(0xFF00E676)
val GlowRuby = Color(0xFFFF1744)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = CyberCyan,
    secondary = NeonEmerald,
    background = MidnightDb,
    surface = DarkCarbon,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)
