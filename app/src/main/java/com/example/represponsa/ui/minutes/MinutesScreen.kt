package com.example.represponsa.ui.minutes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.represponsa.R
import com.example.represponsa.ui.commons.EmptyState
import com.example.represponsa.ui.commons.TopBar

@Composable
fun MinutesScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Atas",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            EmptyState(
                imageRes = R.drawable.ic_minutes_empty_state,
                message = "Ainda não existem atas.",
                buttonText = "Voltar",
                onButtonClick = onNavigateBack
            )
        }
    }
}