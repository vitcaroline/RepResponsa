package com.example.represponsa.ui.assignment.editAssignment

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.R
import com.example.represponsa.di.AssignmentViewModelFactory
import com.example.represponsa.model.Assignment
import com.example.represponsa.ui.assignment.assigmentsList.viewModel.AssignmentViewModel
import com.example.represponsa.ui.assignment.removeAssignment.AssignmentListSelectable
import com.example.represponsa.ui.commons.EmptyState
import com.example.represponsa.ui.commons.TopBar

@Composable
fun EditAssignmentScreen(
    onNavigateBack: () -> Unit,
    onEditAssignment: (Assignment) -> Unit,
    viewModel: AssignmentViewModel = viewModel(factory = AssignmentViewModelFactory)
) {
    val assignments by viewModel.assignments
    val isLoading by viewModel.isLoading

    var selectedId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(title = "Editar Tarefa", onBackClick = onNavigateBack)
        },
        floatingActionButton = {
            if (selectedId != null) {
                FloatingActionButton(
                    onClick = {
                        val assignment = assignments.find { it.id == selectedId }
                        if (assignment != null) {
                            onEditAssignment(assignment)
                        } else {
                            Toast.makeText(context, "Tarefa não encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Selecionado")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
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
                        message = "Sem tarefas para editar",
                        buttonText = "Voltar",
                        onButtonClick = onNavigateBack
                    )
                }

                else -> {
                    AssignmentListSelectable(
                        assignments = assignments,
                        selectedIds = selectedId?.let { setOf(it) } ?: emptySet(),
                        onSelectionChange = { id, checked ->
                            selectedId = if (checked) id else null
                        }
                    )
                }
            }
        }
    }
}