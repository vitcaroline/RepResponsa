package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.assignment.assigmentsList.AssignmentScreen
import com.example.represponsa.presentation.ui.receipts.SubmitReceiptScreen

fun NavGraphBuilder.receiptNavGraph(navController: NavController) {
    composable("receipts"){
        SubmitReceiptScreen(
            onNavigateBack = { navController.popBackStack() },
        )
    }
}