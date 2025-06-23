package com.example.represponsa.presentation.ui.receipts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.represponsa.presentation.ui.commons.TopBar

@Composable
fun SubmitReceiptScreen(
    onNavigateBack: () -> Unit,
) {
    Scaffold(
    topBar = {
        TopBar(
            title = "Comprovantes",
            onBackClick = onNavigateBack
        )
    }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}