package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.home.HomeScreen
import com.example.represponsa.presentation.ui.home.SettingsScreen
import com.example.represponsa.presentation.ui.republic.editRepublic.EditRepublicScreen

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    composable("home") {
        HomeScreen(
            onLogout = {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            onNavigateToAssignments = { navController.navigate("assignments") },
            onNavigateToMinutes = { navController.navigate("minutes") },
            onNavigateToReceipts = { navController.navigate("receipts") },
            onNavigateToResidentsList = { navController.navigate("residents-list") },
            onNavigateToProfile = { navController.navigate("profile-info") },
            onNavigateToSettings = { navController.navigate("settings") }
        )
    }
    composable("settings") {
        SettingsScreen(
            onNavigateBack = { navController.popBackStack()},
            onEditRepublic = { navController.navigate("edit-republic") },
            onRemoveResident = {}
        )
    }
    composable("edit-republic") {
        EditRepublicScreen(
            onNavigateBack = { navController.popBackStack()},
        )
    }
}