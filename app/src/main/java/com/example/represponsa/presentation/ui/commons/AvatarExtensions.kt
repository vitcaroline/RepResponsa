package com.example.represponsa.presentation.ui.commons

import androidx.compose.ui.graphics.Color

fun getColorFromName(name: String): Color {
    val hash = name.hashCode()
    val r = (hash shr 16 and 0xFF) / 255f
    val g = (hash shr 8 and 0xFF) / 255f
    val b = (hash and 0xFF) / 255f
    return Color(r, g, b)
}

fun getInitials(name: String): String {
    return name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")
}