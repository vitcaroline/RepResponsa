package com.example.represponsa.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.receipts.ResidentsPaymentScreen
import com.example.represponsa.presentation.ui.receipts.UploadReceiptScreen
import com.example.represponsa.presentation.ui.receipts.viewModel.RentPaymentConfigViewModel

fun NavGraphBuilder.receiptNavGraph(navController: NavController) {
    composable("receipts") { backStackEntry ->
        val viewModel: RentPaymentConfigViewModel = hiltViewModel(backStackEntry)
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(uiState.isAuthorized, uiState.isLoading) {
            if (!uiState.isLoading && !uiState.isAuthorized) {
                navController.navigate("upload_receipt") {
                    popUpTo("receipts") { inclusive = true }
                }
            }
        }

        if (!uiState.isLoading && uiState.isAuthorized) {
            ResidentsPaymentScreen(
                onNavigateBack = { navController.popBackStack() },
                rentPaymentConfigViewModel = viewModel
            )
        }
    }

    composable("upload_receipt") {
        UploadReceiptScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}