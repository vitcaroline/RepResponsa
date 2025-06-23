package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.represponsa.presentation.ui.assignment.assigmentsList.AssignmentScreen
import com.example.represponsa.presentation.ui.assignment.createAssignment.CreateAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentDetailsScreen
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.removeAssignment.RemoveAssignmentScreen

fun NavGraphBuilder.assignmentNavGraph(navController: NavController) {
    composable("assignments") {
        AssignmentScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCreateAssignment = { navController.navigate("create-assignment") },
            onNavigateToEditAssignment = { navController.navigate("edit-assignment") },
            onNavigateToRemoveAssignment = { navController.navigate("remove-assignment") }
        )
    }

    composable("create-assignment") {
        CreateAssignmentScreen(
            onAssignmentCreated = { navController.popBackStack() },
            onNavigateBack = { navController.popBackStack() },
        )
    }

    composable("edit-assignment") {
        EditAssignmentScreen(
            onNavigateBack = { navController.popBackStack() },
            navController = navController
        )
    }

    composable(
        "edit-assignment-details/{assignmentId}",
        arguments = listOf(navArgument("assignmentId") { type = NavType.StringType })
    ) { backStackEntry ->
        val assignmentId = backStackEntry.arguments?.getString("assignmentId") ?: return@composable

        EditAssignmentDetailsScreen(
            assignmentId = assignmentId,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAssignmentList = { navController.navigate("assignments") }
        )
    }

    composable("remove-assignment") {
        RemoveAssignmentScreen(onNavigateBack = { navController.popBackStack() })
    }
}