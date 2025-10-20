package com.example.represponsa.presentation.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onPrimaryContainer = Purple80,
    surfaceContainer = LightOrange,
    surfaceBright = BrightOrange
)

// Azul Pastel
private val BluePastel = lightColorScheme(
    primary = Color(0xFF8AB4F8),
    secondary = Color(0xFFBFD8FF),
    tertiary = Color(0xFFAEC6FF),
    onPrimaryContainer = Color(0xFFDCE6FF),
    surfaceContainer = Color(0xFFEAF0FF),
    surfaceBright = Color(0xFFF5F9FF)
)

// Rosa Pastel
private val PinkPastel = lightColorScheme(
    primary = Color(0xFFF8BBD0),
    secondary = Color(0xFFFFD6E8),
    tertiary = Color(0xFFFFC0D9),
    onPrimaryContainer = Color(0xFFFFE4F1),
    surfaceContainer = Color(0xFFFFF0F5),
    surfaceBright = Color(0xFFFFF5FA)
)

// Laranja Pastel
private val OrangePastel = lightColorScheme(
    primary = Color(0xFFFFCC80),
    secondary = Color(0xFFFFE0B2),
    tertiary = Color(0xFFFFD8A6),
    onPrimaryContainer = Color(0xFFFFF1E0),
    surfaceContainer = Color(0xFFFFF5EB),
    surfaceBright = Color(0xFFFFFAF5)
)

// Verde Pastel
private val GreenPastel = lightColorScheme(
    primary = Color(0xFFA5D6A7),
    secondary = Color(0xFFC8E6C9),
    tertiary = Color(0xFFB9E3B5),
    onPrimaryContainer = Color(0xFFE8F5E9),
    surfaceContainer = Color(0xFFF1FAF1),
    surfaceBright = Color(0xFFF5FBF5)
)

enum class RepublicTheme {
    BLUE, PINK, ORANGE, GREEN
}

@Composable
fun RepResponsaTheme(
    selectedTheme: RepublicTheme = RepublicTheme.BLUE,
    content: @Composable () -> Unit
) {
    val colors = when (selectedTheme) {
        RepublicTheme.BLUE -> BluePastel
        RepublicTheme.PINK -> PinkPastel
        RepublicTheme.ORANGE -> OrangePastel
        RepublicTheme.GREEN -> GreenPastel
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}