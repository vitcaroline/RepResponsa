package com.example.represponsa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.represponsa.ui.home.HomeScreen
import com.example.represponsa.ui.login.LoginScreen
import com.example.represponsa.ui.registerRepublic.RegisterRepublicScreen
import com.example.represponsa.ui.registerUser.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
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
                }
            )
        }
    }
}