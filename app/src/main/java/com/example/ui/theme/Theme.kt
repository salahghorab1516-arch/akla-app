package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = GoldenAmberLight,
    onPrimary = Color(0xFF451A03),
    primaryContainer = GoldenAmber,
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = ParsleyGreenLight,
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = ParsleyGreen,
    onSecondaryContainer = Color(0xFFD1FAE5),
    tertiary = StreetRedLight,
    onTertiary = Color(0xFF450A0A),
    background = Color(0xFF181512),
    surface = Color(0xFF24201C),
    onBackground = Color(0xFFF3EFEA),
    onSurface = Color(0xFFF3EFEA),
    surfaceVariant = Color(0xFF38322B),
    onSurfaceVariant = Color(0xFFD7CCC8)
)

private val LightColorScheme = lightColorScheme(
    primary = StreetRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410002),
    secondary = GoldenAmber,
    onSecondary = Color.White,
    secondaryContainer = GoldenAmberPale,
    onSecondaryContainer = LinseedOilBrown,
    tertiary = ParsleyGreen,
    onTertiary = Color.White,
    tertiaryContainer = ParsleyGreenPale,
    onTertiaryContainer = Color(0xFF022C22),
    background = StreetBackground,
    surface = StreetSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = StreetSurfaceVariant,
    onSurfaceVariant = TextSecondary
)

@Composable
fun AklaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep Egyptian street food brand colors consistent
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
