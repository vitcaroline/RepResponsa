package com.example.represponsa.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.represponsa.presentation.ui.assignment.assigmentsList.AssignmentScreen
import com.example.represponsa.presentation.ui.assignment.createAssignment.CreateAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.createAssignment.ReuseAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel.CreateAssignmentViewModel
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentDetailsScreen
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.removeAssignment.RemoveAssignmentScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.assignmentNavGraph(
    navController: NavController,
)
{
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
            onAssignmentCreated = { navController.navigate("assignments") { popUpTo("assignments") { inclusive = true }} },
            onNavigateBack = { navController.popBackStack() },
            onNavigateToReuseAssignment = { navController.navigate("reuseAssignment") }
        )
    }

    composable("reuseAssignment") {
        val parentEntry = remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry("create-assignment")
        }
        val createAssignmentViewModel = hiltViewModel<CreateAssignmentViewModel>(parentEntry)

        ReuseAssignmentScreen(
            onSelectAssignment = { assignment ->
                createAssignmentViewModel.fillFromExistingAssignment(assignment)
                navController.popBackStack()
            },
            onNavigateBack = { navController.popBackStack() }
        )
    }

    composable("edit-assignment") {
        EditAssignmentScreen(
            onNavigateBack = { navController.navigate("assignments") { popUpTo("assignments") { inclusive = true }} },
            navController = navController
        )
    }

    composable(
        route = "edit-assignment-details/{assignmentId}",
        arguments = listOf(navArgument("assignmentId") { type = NavType.StringType })
    ) {
        EditAssignmentDetailsScreen(
            onNavigateBack = {
                navController.navigate("assignments") {
                    popUpTo("assignments") { inclusive = true }
                }
            },
            onNavigateToAssignmentList = {
                navController.navigate("assignments")
            }
        )
    }

    composable("remove-assignment") {
        RemoveAssignmentScreen(
            onNavigateBack = { navController.navigate("assignments") { popUpTo("assignments") { inclusive = true } } }
        )
    }
}