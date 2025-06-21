package com.example.represponsa.presentation.ui.assignment.editAssignment

import android.net.Uri
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.represponsa.R
import com.example.represponsa.di.AssignmentListViewModelFactory
import com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel.AssignmentListViewModel
import com.example.represponsa.presentation.ui.assignment.commons.AssignmentListSelectable
import com.example.represponsa.presentation.ui.commons.EmptyState
import com.example.represponsa.presentation.ui.commons.TopBar
import com.google.gson.Gson

@Composable
fun EditAssignmentScreen(
    onNavigateBack: () -> Unit,
    navController: NavController,
    viewModel: AssignmentListViewModel = viewModel(factory = AssignmentListViewModelFactory)
) {
    val assignments by viewModel.assignments
    val isLoading by viewModel.isLoading

    var selectedId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

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
            TopBar(title = "Editar Tarefa", onBackClick = onNavigateBack)
        },
        floatingActionButton = {
            if (selectedId != null) {
                FloatingActionButton(
                    onClick = {
                        val assignment = assignments.find { it.id == selectedId }
                        if (assignment != null) {
                            val json = Uri.encode(Gson().toJson(assignment))
                            navController.navigate("edit-assignment-details/$json")
                        } else {
                            Toast.makeText(context, "Tarefa não encontrada", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary
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
