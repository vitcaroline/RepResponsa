package com.example.represponsa.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.HomeViewModelFactory
import com.example.represponsa.presentation.ui.commons.HomeTopBar
import com.example.represponsa.presentation.ui.home.viewModel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToMinutes: () -> Unit,
    onNavigateToReceipts: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory)
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.secondary,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "República ${viewModel.republicName.value}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Lista de Tarefas", tint = Color(0xFF004D40)) },
                    label = { Text("Tarefas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToAssignments()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Create, contentDescription = "Lista de Tarefas", tint = Color(0xFF004D40)) },
                    label = { Text("Atas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToMinutes()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = "Lista de Tarefas", tint = Color(0xFF004D40)) },
                    label = { Text("Comprovantes") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToReceipts()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.ExitToApp, contentDescription = "Lista de Tarefas", tint = Color(0xFF004D40)) },
                    label = { Text("Sair") },
                    selected = false,
                    onClick = {
                        viewModel.logout()
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    userName = viewModel.userName.value,
                    onOptionSelected = { option ->
                        when (option) {
                            "profile" -> { /* abrir tela de perfil */ }
                            "config" -> { /* abrir tela de configurações */ }
                            "logout" -> {
                                viewModel.logout()
                                onLogout()
                            }
                        }
                    },
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Bem-vindo! ${viewModel.userName.value}", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}