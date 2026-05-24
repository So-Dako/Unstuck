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
    primary = CherryBlossomPink,
    surfaceVariant = CherryBlossomPink.copy(alpha = 0.3f),

    background = VeryDarkPink,
    surface = VeryDarkPink,

    onSurface = CherryBlossomPink,
    onBackground = CherryBlossomPink
)

private val LightColorScheme = lightColorScheme(
    primary = CherryBlossomPink,
    surfaceVariant = CherryBlossomPink.copy(alpha = 0.3f),

    background = Color.White,
    surface = Color.White,

    onSurface = CharcoalBlue,
    onBackground = CharcoalBlue

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