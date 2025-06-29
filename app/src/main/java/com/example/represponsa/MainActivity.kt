package com.example.represponsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.example.represponsa.presentation.navigation.AppNavigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.represponsa.presentation.ui.theme.RepResponsaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepResponsaTheme {
                val systemUiController = rememberSystemUiController()

                val statusBarColor = MaterialTheme.colorScheme.onPrimaryContainer
                val useDarkIcons = true

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = useDarkIcons
                    )
                }

                AppNavigation()
            }
        }
    }
}