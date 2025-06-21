package com.example.represponsa.presentation.ui.assignment.removeAssignment

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.R
import com.example.represponsa.di.RemoveAssignmentViewModelFactory
import com.example.represponsa.presentation.ui.assignment.removeAssignment.viewModel.RemoveAssignmentViewModel
import com.example.represponsa.presentation.ui.commons.EmptyState
import com.example.represponsa.presentation.ui.commons.TopBar

@Composable
fun RemoveAssignmentScreen(
    onNavigateBack: () -> Unit,
    viewModel: RemoveAssignmentViewModel = viewModel(factory = RemoveAssignmentViewModelFactory)
) {
    val assignments by viewModel.assignments
    val isLoading by viewModel.isLoading

    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(title = "Remover Tarefa", onBackClick = onNavigateBack)
        },
        floatingActionButton = {
            if (selectedIds.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                    viewModel.deleteAssignments(selectedIds.toList()) {
                        Toast.makeText(context, "Tarefa removida com sucesso!", Toast.LENGTH_SHORT).show()
                        selectedIds = emptySet()
                    }
                },
                    containerColor = MaterialTheme.colorScheme.tertiary
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover Selecionados")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                assignments.isEmpty() -> {
                    EmptyState(
                        imageRes = R.drawable.ic_assignments_empty_state,
                        message = "Sem tarefas para remover",
                        buttonText = "Voltar",
                        onButtonClick = onNavigateBack
                    )
                }

                else -> {
                    AssignmentListSelectable(
                        assignments = assignments,
                        selectedIds = selectedIds,
                        onSelectionChange = { id, checked ->
                            selectedIds = if (checked) {
                                selectedIds + id
                            } else {
                                selectedIds - id
                            }
                        }
                    )
                }
            }
        }
    }
}