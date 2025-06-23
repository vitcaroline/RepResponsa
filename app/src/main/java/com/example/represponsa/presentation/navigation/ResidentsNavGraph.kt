package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.residents.ResidentsListScreen

fun NavGraphBuilder.residentsNavGraph(navController: NavController) {
    composable("residents-list") {
        ResidentsListScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}