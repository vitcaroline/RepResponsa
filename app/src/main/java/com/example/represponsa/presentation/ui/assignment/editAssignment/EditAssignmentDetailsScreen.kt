package com.example.represponsa.presentation.ui.assignment.editAssignment

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.di.EditAssignmentViewModelFactory
import com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel.EditAssignmentViewModel
import com.example.represponsa.presentation.ui.commons.DatePickerButton
import com.example.represponsa.presentation.ui.commons.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAssignmentDetailsScreen(
    assignmentToEdit: Assignment,
    onNavigateBack: () -> Unit,
    viewModel: EditAssignmentViewModel = viewModel(factory = EditAssignmentViewModelFactory)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state by viewModel.state
    val residents by viewModel.residents

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startEditing(assignmentToEdit)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Editar Tarefa",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(24.dp)
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onEditTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    state.titleError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::onEditDescriptionChange,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.selectedResident?.firstName ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Morador Responsável") },
                    isError = state.residentError != null,
                    supportingText = {
                        state.residentError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    residents.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.firstName) },
                            onClick = {
                                viewModel.onResidentSelected(user)
                                expanded = false
                            }
                        )
                    }
                }
            }

            DatePickerButton(
                selectedDate = state.dueDate,
                onDateSelected = viewModel::onEditDueDateChange
            )
            state.dateError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!viewModel.validateFields()) return@Button

                scope.launch {
                    viewModel.saveEditedAssignment(
                        onSuccess = {
                            Toast.makeText(context, "Tarefa editada com sucesso!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }) {
                Text("Salvar Alterações")
            }
        }
    }
}