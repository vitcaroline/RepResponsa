package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.assignment.assigmentsList.AssignmentScreen

fun NavGraphBuilder.receiptNavGraph(navController: NavController) {
    composable("receipts"){
        AssignmentScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCreateAssignment = { navController.navigate("create-assignment") },
            onNavigateToEditAssignment = { navController.popBackStack() },
            onNavigateToRemoveAssignment = { navController.navigate("remove-assignment") }
        )
    }
}