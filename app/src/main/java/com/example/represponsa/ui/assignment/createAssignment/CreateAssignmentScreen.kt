package com.example.represponsa.ui.assignment.createAssignment

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.CreateAssignmentViewModelFactory
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.represponsa.ui.assignment.createAssignment.viewModel.CreateAssignmentViewModel
import com.example.represponsa.ui.commons.DatePickerButton
import com.example.represponsa.ui.commons.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssignmentScreen(
    onAssignmentCreated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CreateAssignmentViewModel = viewModel(factory = CreateAssignmentViewModelFactory)
) {

    val scope = rememberCoroutineScope()

    val state by viewModel.state
    val residents by viewModel.residents

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Criar nova tarefa",
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
                onValueChange = viewModel::onTitleChange,
                label = { Text("Título") },
                isError = state.titleError != null,
                supportingText = {
                    state.titleError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
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

            Spacer(modifier = Modifier.height(8.dp))

            DatePickerButton(
                selectedDate = state.dueDate,
                onDateSelected = viewModel::onDateSelected
            )
            state.dateError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!viewModel.validateFields()) return@Button

                scope.launch {
                    viewModel.createAssignment(
                        onSuccess = {
                            Toast.makeText(context, "Tarefa criada com sucesso!", Toast.LENGTH_SHORT).show()
                            onAssignmentCreated()
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }) {
                Text("Salvar")
            }
        }
    }
}
