package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.represponsa.presentation.ui.minutes.createMinute.CreateMinuteScreen
import com.example.represponsa.presentation.ui.minutes.editMinute.EditMinuteScreen
import com.example.represponsa.presentation.ui.minutes.minutesList.MinuteDetailsScreen
import com.example.represponsa.presentation.ui.minutes.minutesList.MinutesScreen

fun NavGraphBuilder.minuteNavGraph(navController: NavController) {
    composable("minutes") {
        MinutesScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCreateMinute = { navController.navigate("create-minute") },
            onNavigateToMinuteDetail = { minute ->
                navController.navigate("minute-details/${minute.id}")
            }
        )
    }
    composable(
    route = "minute-details/{minuteId}",
    arguments = listOf(navArgument("minuteId") { type = NavType.StringType })
    ) { backStackEntry ->
        val minuteId = backStackEntry.arguments?.getString("minuteId") ?: return@composable

        MinuteDetailsScreen(
            minuteId = minuteId,
            onNavigateBack = { navController.navigate("minutes") { popUpTo("minutes") { inclusive = true } }},
            onNavigateToEdit = { minute ->
                navController.navigate("edit-minute/${minute.id}")
            }
        )
    }
    composable(
    "edit-minute/{minuteId}",
    arguments = listOf(navArgument("minuteId") { type = NavType.StringType })
    ) { backStackEntry ->
        val minuteId = backStackEntry.arguments?.getString("minuteId") ?: return@composable
        EditMinuteScreen(
            minuteId = minuteId,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToList = { navController.navigate("minutes") { popUpTo("minutes") { inclusive = true } } }
        )
    }
    composable("create-minute"){
        CreateMinuteScreen(
            onMinuteCreated = { navController.navigate("minutes") { popUpTo("minutes") { inclusive = true } }},
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
