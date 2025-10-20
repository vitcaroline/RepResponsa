package com.example.represponsa.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.R
import com.example.represponsa.data.cacheConfig.UserPreferences
import com.example.represponsa.data.cacheConfig.UserPreferences.republicThemeFlow
import com.example.represponsa.presentation.ui.commons.HomeTopBar
import com.example.represponsa.presentation.ui.home.viewModel.HomeViewModel
import com.example.represponsa.presentation.ui.theme.RepublicTheme
import com.example.represponsa.presentation.ui.theme.RepResponsaTheme
import com.example.represponsa.presentation.ui.theme.ThemeSelectionBottomSheet
import kotlinx.coroutines.CoroutineScope
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
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedTheme by context.republicThemeFlow.collectAsState(initial = RepublicTheme.AZUL)
    var showThemeSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.reloadHomeData() }

    RepResponsaTheme(selectedTheme = selectedTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                HomeDrawer(
                    viewModel = viewModel,
                    scope = scope,
                    drawerState = drawerState,
                    onLogout = onLogout,
                    onNavigateToAssignments = onNavigateToAssignments,
                    onNavigateToMinutes = onNavigateToMinutes,
                    onNavigateToReceipts = onNavigateToReceipts,
                    onNavigateToResidentsList = onNavigateToResidentsList,
                    onNavigateToSettings = onNavigateToSettings,
                    onShowThemeSheet = { showThemeSheet = true }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    HomeTopBar(
                        userName = viewModel.nickName.value,
                        onOptionSelected = { option ->
                            when (option) {
                                "profile" -> onNavigateToProfile()
                                "config" -> onNavigateToSettings()
                                "logout" -> {
                                    viewModel.logout()
                                    onLogout()
                                }
                            }
                        },
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    if (viewModel.isLoading.value) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        PointsDashboard(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(10.dp),
                            residentsPoints = viewModel.residentsPoints.value,
                            pendingAssignments = viewModel.pendingAssignments.value,
                            onNavigateToAssignments = { onNavigateToAssignments() }
                        )
                    }
                }

                if (showThemeSheet) {
                    ThemeSelectionBottomSheet(
                        onDismiss = { showThemeSheet = false },
                        onThemeSelected = { theme ->
                            scope.launch { UserPreferences.saveRepublicTheme(context, theme) }
                            showThemeSheet = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeDrawer(
    viewModel: HomeViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onLogout: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToMinutes: () -> Unit,
    onNavigateToReceipts: () -> Unit,
    onNavigateToResidentsList: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onShowThemeSheet: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "República ${viewModel.republicName.value}",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Divider()
        DrawerItem(Icons.Default.List, "Tarefas") {
            scope.launch { drawerState.close() }
            onNavigateToAssignments()
        }
        DrawerItem(Icons.Outlined.Create, "Atas") {
            scope.launch { drawerState.close() }
            onNavigateToMinutes()
        }
        DrawerItem(painter = painterResource(R.drawable.ic_receipts), "Comprovantes") {
            scope.launch { drawerState.close() }
            onNavigateToReceipts()
        }
        DrawerItem(Icons.Outlined.Face, "Moradores") {
            scope.launch { drawerState.close() }
            onNavigateToResidentsList()
        }
        Divider()
        DrawerItem(Icons.Outlined.Settings, "Configurações") {
            scope.launch { drawerState.close() }
            onNavigateToSettings()
        }
        DrawerItem(Icons.Outlined.ColorLens, "Trocar cor de tema") {
            scope.launch { drawerState.close() }
            onShowThemeSheet()
        }
        Divider()
        DrawerItem(Icons.Outlined.ExitToApp, "Sair") {
            viewModel.logout()
            scope.launch { drawerState.close() }
            onLogout()
        }
    }
}

@Composable
private fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = label, tint = Color(0xFF004D40)) },
        label = { Text(label) },
        selected = false,
        onClick = onClick
    )
}

@Composable
private fun DrawerItem(painter: androidx.compose.ui.graphics.painter.Painter, label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(painter = painter, contentDescription = label, tint = Color(0xFF004D40)) },
        label = { Text(label) },
        selected = false,
        onClick = onClick
    )
}

