package com.example.represponsa.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.R
import com.example.represponsa.presentation.ui.commons.HomeTopBar
import com.example.represponsa.presentation.ui.home.viewModel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToMinutes: () -> Unit,
    onNavigateToReceipts: () -> Unit,
    onNavigateToResidentsList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.reloadHomeData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.secondary,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "República ${viewModel.republicName.value}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Lista de Tarefas", tint = Color(0xFF004D40)) },
                    label = {
                        Text("Tarefas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToAssignments()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Create, contentDescription = "Lista de Atas", tint = Color(0xFF004D40)) },
                    label = {
                        Text("Atas") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToMinutes()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(painter = painterResource(R.drawable.ic_receipts), contentDescription = "Comprovantes", tint = Color(0xFF004D40)) },
                    label = {
                        Text("Comprovantes") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToReceipts()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Face, contentDescription = "Moradores", tint = Color(0xFF004D40)) },
                    label = { Text("Moradores")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToResidentsList()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.ExitToApp, contentDescription = "Sair", tint = Color(0xFF004D40)) },
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
                    userName = viewModel.nickName.value,
                    onOptionSelected = { option ->
                        when (option) {
                            "profile" -> { onNavigateToProfile() }
                            "config" -> { onNavigateToSettings() }
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
                    .padding(innerPadding)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    PointsDashboard(
                        residentsPoints = viewModel.residentsPoints.value,
                        pendingAssignments = viewModel.pendingAssignments.value
                    )
                }
            }

        }
    }
}