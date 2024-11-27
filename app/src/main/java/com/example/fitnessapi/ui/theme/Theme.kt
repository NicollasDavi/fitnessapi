package com.example.fitnessapi.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LightColorScheme = lightColorScheme(
    primary = Green500,
    secondary = Blue500,
    background = Background,
    surface = Surface,
    onPrimary = OnPrimary,
    onSurface = OnSurface,
    error = Color.Red
)

val DarkColorScheme = darkColorScheme(
    primary = Green700,
    secondary = Blue200,
    background = Color.Black,
    surface = Color.Gray,
    onPrimary = OnPrimary,
    onSurface = Color.White,
    error = Color.Red
)

@Composable
fun FitnessAppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Aqui você pode ajustar a tipografia também, se necessário
        content = content
    )
}