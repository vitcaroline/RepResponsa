package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.receipts.ResidentsPaymentScreen

fun NavGraphBuilder.receiptNavGraph(navController: NavController) {
    composable("receipts"){
        ResidentsPaymentScreen(
            onNavigateBack = { navController.popBackStack() },
        )
    }
}