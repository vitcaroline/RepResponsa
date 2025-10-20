package com.example.represponsa.presentation.ui.residents.removeResidents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.data.model.User
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.commons.UserAvatar
import com.example.represponsa.presentation.ui.residents.removeResidents.viewModel.RemoveResidentsViewModel

@Composable
fun RemoveResidentsScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemoveResidentsViewModel = hiltViewModel()
) {
    val residents by viewModel.residents
    val selectedResidents by viewModel.selectedResidents
    val isLoading by viewModel.isLoading
    val isRemoving by viewModel.isRemoving

    var showConfirmationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(title = "Remover Moradores", onBackClick = onNavigateBack)
        },
        floatingActionButton = {
            if (selectedResidents.isNotEmpty() && !isRemoving) {
                FloatingActionButton(
                    onClick = { viewModel.removeSelectedResidents() },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover selecionados")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = "Lista de Moradores",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))

                    when {
                        isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        residents.isEmpty() -> Text("Nenhum morador encontrado.")
                        else -> LazyColumn {
                            items(residents) { user ->
                                ResidentSelectableItem(
                                    user = user,
                                    isSelected = selectedResidents.contains(user.uid),
                                    onSelect = { viewModel.toggleSelection(user.uid) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isRemoving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissConfirmationDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.removeSelectedResidents() }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissConfirmationDialog() }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Remover moradores") },
                text = { Text("Tem certeza que deseja remover os moradores selecionados?") }
            )
        }
    }
}


@Composable
fun ResidentSelectableItem(
    user: User,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isSelected, onCheckedChange = { onSelect() })
        UserAvatar(modifier = Modifier.padding(8.dp), userName = user.userName)
        Spacer(Modifier.width(8.dp))
        Column {
            Text("${user.userName} (${user.nickName})", style = MaterialTheme.typography.bodyLarge)
            Text(user.role.lowercase(), style = MaterialTheme.typography.bodyMedium)
            Text(user.email, style = MaterialTheme.typography.bodySmall, color = Color.Black)
        }
    }
    Divider()
}

