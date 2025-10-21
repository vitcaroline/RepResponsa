package com.example.represponsa

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.represponsa.data.cacheConfig.UserPreferences.republicThemeFlow
import com.example.represponsa.presentation.navigation.AppNavigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.represponsa.presentation.ui.theme.RepResponsaTheme
import com.example.represponsa.presentation.ui.theme.RepublicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

        setContent {
            val context = LocalContext.current
            val selectedTheme by context.republicThemeFlow.collectAsState(initial = RepublicTheme.AZUL)

            RepResponsaTheme(selectedTheme = selectedTheme) {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = MaterialTheme.colorScheme.onPrimaryContainer

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = true
                    )
                }

                AppNavigation()
            }

        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}