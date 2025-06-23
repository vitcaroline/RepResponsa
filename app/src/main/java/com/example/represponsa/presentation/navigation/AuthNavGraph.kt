package com.example.represponsa.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.represponsa.presentation.ui.login.LoginScreen
import com.example.represponsa.presentation.ui.login.SplashScreen
import com.example.represponsa.presentation.ui.profile.ProfileScreen
import com.example.represponsa.presentation.ui.registerRepublic.RegisterRepublicScreen
import com.example.represponsa.presentation.ui.registerUser.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable("splash") {
        SplashScreen(
            onNavigateToHome = { navController.navigate("home") { popUpTo("splash") { inclusive = true } } },
            onNavigateToLogin = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } }
        )
    }
    composable("login") {
        LoginScreen(
            onLoginSuccess = { navController.navigate("home") },
            onNavigateToRegister = { navController.navigate("register-user") },
            onNavigateToCreateRepublic = { navController.navigate("register-republic") }
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
    composable("profile-info") {
        ProfileScreen (
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
