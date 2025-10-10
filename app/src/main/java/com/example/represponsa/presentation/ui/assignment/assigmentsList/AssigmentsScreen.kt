package com.example.represponsa.presentation.ui.assignment.assigmentsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.represponsa.R
import com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel.AssignmentListViewModel
import com.example.represponsa.presentation.ui.commons.EmptyState
import com.example.represponsa.presentation.ui.commons.ExpandableFab
import com.example.represponsa.presentation.ui.commons.FabAction
import com.example.represponsa.presentation.ui.commons.TopBar

@Composable
fun AssignmentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreateAssignment: () -> Unit,
    onNavigateToEditAssignment: () -> Unit,
    onNavigateToRemoveAssignment: () -> Unit,
    viewModel: AssignmentListViewModel = hiltViewModel()
) {
    val assignments by viewModel.assignments

    val isLoading by viewModel.isLoading

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchAssignments()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Tarefas",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            if (viewModel.canManageAssignments.value) {
                ExpandableFab(
                    actions = listOf(
                        FabAction(
                            icon = Icons.Default.Add,
                            label = "Adicionar",
                            onClick = onNavigateToCreateAssignment
                        ),
                        FabAction(
                            icon = Icons.Default.Edit,
                            label = "Editar",
                            onClick = onNavigateToEditAssignment
                        ),
                        FabAction(
                            icon = Icons.Default.Delete,
                            label = "Excluir",
                            onClick = onNavigateToRemoveAssignment
                        )
                    )
                )
            }
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            assignments.isEmpty() -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    EmptyState(
                        imageRes = R.drawable.ic_assignments_empty_state,
                        message = "Você ainda não tem tarefas.",
                        buttonText = "Voltar",
                        onButtonClick = onNavigateBack
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Surface(
                        tonalElevation = 4.dp,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FilterChip(
                                selected = viewModel.showOnlyMyAssignments.value,
                                onClick = { viewModel.setFilter(onlyMine = true) },
                                label = { Text("Minhas Tarefas") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Minhas Tarefas", tint = Color.LightGray) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color.Black
                                )
                            )
                            FilterChip(
                                selected = !viewModel.showOnlyMyAssignments.value,
                                onClick = { viewModel.setFilter(onlyMine = false) },
                                label = { Text("Todas Tarefas") },
                                leadingIcon = { Icon(Icons.Default.List, contentDescription = "Todas Tarefas", tint = Color.LightGray) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color.Black
                                )
                            )
                        }
                    }
                    AssignmentList(modifier = Modifier, assignments = assignments)
                }
            }
        }
    }
}