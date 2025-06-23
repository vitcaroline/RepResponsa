package com.example.represponsa.presentation.ui.minutes.createMinute

data class MinuteUiState(
    val title: String = "",
    val body: String = "",
    val titleError: String? = null,
    val bodyError: String? = null,
)