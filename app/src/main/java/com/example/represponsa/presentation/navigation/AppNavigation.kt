package com.example.represponsa.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.represponsa.presentation.ui.assignment.assigmentsList.AssignmentScreen
import com.example.represponsa.presentation.ui.assignment.createAssignment.CreateAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentDetailsScreen
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentScreen
import com.example.represponsa.presentation.ui.assignment.removeAssignment.RemoveAssignmentScreen
import com.example.represponsa.presentation.ui.home.HomeScreen
import com.example.represponsa.presentation.ui.login.LoginScreen
import com.example.represponsa.presentation.ui.minutes.createMinute.CreateMinuteScreen
import com.example.represponsa.presentation.ui.minutes.editMinute.EditMinuteScreen
import com.example.represponsa.presentation.ui.minutes.minutesList.MinuteDetailsScreen
import com.example.represponsa.presentation.ui.minutes.minutesList.MinutesScreen
import com.example.represponsa.presentation.ui.registerRepublic.RegisterRepublicScreen
import com.example.represponsa.presentation.ui.registerUser.RegisterScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home")},
                onNavigateToRegister = { navController.navigate("register-user") },
                onNavigateToCreateRepublic = { navController.navigate("register-republic")}
            )
        }
        composable("register-user") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") { popUpTo("register-user") { inclusive = true } } },
                onNavigateBack    = { navController.popBackStack() },
                onNavigateToRegisterRepublic = {navController.navigate("register-republic")}
            )
        }
        composable("register-republic") {
            RegisterRepublicScreen(
                onCreateSuccess = { navController.navigate("login") { popUpTo("register-republic") { inclusive = true } } },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToAssignments = { navController.navigate("assignments") },
                onNavigateToMinutes = { navController.navigate("minutes") },
                onNavigateToReceipts = { navController.navigate("receipts") }
            )
        }
        composable("assignments"){
            AssignmentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateAssignment = { navController.navigate("create-assignment") },
                onNavigateToEditAssignment = { navController.navigate("edit-assignment") },
                onNavigateToRemoveAssignment = { navController.navigate("remove-assignment") }
            )
        }
        composable("create-assignment"){
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
        composable("remove-assignment"){
            RemoveAssignmentScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
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
                onNavigateBack = { navController.popBackStack() },
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
                onNavigateToList = { navController.navigate("minutes") }
            )
        }
        composable("create-minute"){
            CreateMinuteScreen(
                onMinuteCreated = { navController.navigate("minutes") { popUpTo("minutes") { inclusive = true } }},
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("receipts"){
            AssignmentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreateAssignment = { navController.navigate("create-assignment") },
                onNavigateToEditAssignment = { navController.popBackStack() },
                onNavigateToRemoveAssignment = { navController.navigate("remove-assignment") }
            )
        }
    }
}