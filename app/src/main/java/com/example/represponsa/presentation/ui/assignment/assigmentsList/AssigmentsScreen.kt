package com.example.represponsa.presentation.ui.assignment.assigmentsList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.R
import com.example.represponsa.di.assignments.AssignmentListViewModelFactory
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
    viewModel: AssignmentListViewModel = viewModel(factory = AssignmentListViewModelFactory)
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
                Box(modifier = Modifier.padding(innerPadding)) {
                    AssignmentList(assignments = assignments)
                }
            }
        }
    }
}