package com.example.represponsa.presentation.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Azul Pastel
private val BluePastel = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = LightBlue40,
    onPrimaryContainer = Blue80,
    surfaceContainer = LightOrange,
    surfaceBright = BrightOrange
)

// Rosa Pastel
private val PinkPastel = lightColorScheme(
    primary = Pink40,
    secondary = Color(0xFFEE629F),
    tertiary = Color(0xFFF89ABF),
    onPrimaryContainer = Color(0xFFFFE4F1),
    surfaceContainer = Color(0xFFFFF0F5),
    surfaceBright = Color(0xFFFFF5FA)
)

// Laranja Pastel
private val OrangePastel = lightColorScheme(
    primary = Orange40,
    secondary = Color(0xFFF17B5B),
    tertiary = Color(0xFFEE994E),
    onPrimaryContainer = Color(0xFFF5E0C6),
    surfaceContainer = Color(0xFFFFF5EB),
    surfaceBright = Color(0xFFFFFAF5)
)

// Verde Pastel
private val GreenPastel = lightColorScheme(
    primary = Green40,
    secondary = Color(0xFF8AD790),
    tertiary = Color(0xFF7DB738),
    onPrimaryContainer = Color(0xFFCAF8CC),
    surfaceContainer = Color(0xFFF1FAF1),
    surfaceBright = Color(0xFFF5FBF5)
)

enum class RepublicTheme {
    AZUL, ROSA, LARANJA, VERDE
}

@Composable
fun RepResponsaTheme(
    selectedTheme: RepublicTheme = RepublicTheme.AZUL,
    content: @Composable () -> Unit
) {
    val colors = when (selectedTheme) {
        RepublicTheme.AZUL -> BluePastel
        RepublicTheme.ROSA -> PinkPastel
        RepublicTheme.LARANJA -> OrangePastel
        RepublicTheme.VERDE -> GreenPastel
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}