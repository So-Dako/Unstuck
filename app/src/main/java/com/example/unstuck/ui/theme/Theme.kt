package com.example.unstuck.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.*

private val DarkColorScheme = darkColorScheme(
//    primary = CherryBlossomPink,
//    onPrimary = VeryDarkPink,
//
//    background = VeryDarkPink,
//    onBackground = CherryBlossomPink,
//
//    surface = VeryDarkPink,
//    onSurface = CherryBlossomPink,
//
//    surfaceVariant = CherryBlossomPink.copy(alpha = 0.15f),
//    onSurfaceVariant = CherryBlossomPink,
//
//    outline = CherryBlossomPink.copy(alpha = 0.2f)
    primary = OliveKhaki,
    onPrimary = Color.White,

    background = DarkOliveNight,
    onBackground = OliveKhaki,

    surface = DarkOliveNight,
    onSurface = OliveKhaki,

    surfaceVariant = OliveKhaki.copy(alpha = 0.15f),
    onSurfaceVariant = OliveKhaki,

    outline = OliveKhaki.copy(alpha = 0.2f)
)

private val LightColorScheme = lightColorScheme(
//    primary = CherryBlossomPink,
//    onPrimary = Color.White,
//
//    background = Color.White,
//    onBackground = CharcoalBlue,
//
//    surface = Color.White,
//    onSurface = CharcoalBlue,
//
//    surfaceVariant = CherryBlossomPink.copy(alpha = 0.15f),
//    onSurfaceVariant = CharcoalBlue,
//
//    outline = CharcoalBlue.copy(alpha = 0.15f)
    primary = OliveKhaki,
    onPrimary = Color.White,

    background = Color.White,
    onBackground = CharcoalBlue,

    surface = Color.White,
    onSurface = CharcoalBlue,

    surfaceVariant = OliveKhaki.copy(alpha = 0.12f),
    onSurfaceVariant = CharcoalBlue,

    outline = CharcoalBlue.copy(alpha = 0.15f)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun UnstuckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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