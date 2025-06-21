package com.example.represponsa.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.represponsa.ui.assignment.assigmentsList.AssignmentScreen
import com.example.represponsa.ui.assignment.createAssignment.CreateAssignmentScreen
import com.example.represponsa.ui.assignment.editAssignment.EditAssignmentDetailsScreen
import com.example.represponsa.ui.assignment.editAssignment.EditAssignmentScreen
import com.example.represponsa.ui.assignment.removeAssignment.RemoveAssignmentScreen
import com.example.represponsa.ui.home.HomeScreen
import com.example.represponsa.ui.login.LoginScreen
import com.example.represponsa.ui.minutes.MinutesScreen
import com.example.represponsa.ui.registerRepublic.RegisterRepublicScreen
import com.example.represponsa.ui.registerUser.RegisterScreen
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
        composable("edit-assignment"){
            EditAssignmentScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditAssignment = { navController.navigate("edit-assignment-details")}
            )
        }
        composable("edit-assignment-details"){
            EditAssignmentDetailsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("remove-assignment"){
            RemoveAssignmentScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable("minutes"){
            MinutesScreen(
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