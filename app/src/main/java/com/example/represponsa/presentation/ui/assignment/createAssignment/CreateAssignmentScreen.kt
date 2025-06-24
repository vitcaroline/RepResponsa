package com.example.represponsa.presentation.ui.assignment.createAssignment

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.assignment.commons.AssignmentForm
import com.example.represponsa.presentation.ui.assignment.commons.AssignmentFormState
import com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel.CreateAssignmentViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateAssignmentScreen(
    onAssignmentCreated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CreateAssignmentViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.state
    val residents by viewModel.residents
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = "Criar nova tarefa",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        AssignmentForm(
            modifier = Modifier.padding(innerPadding),
            params = AssignmentFormState(
                title = state.title,
                onTitleChange = viewModel::onTitleChange,
                titleError = state.titleError,

                description = state.description,
                onDescriptionChange = viewModel::onDescriptionChange,

                selectedResidents = state.selectedResidents,
                onResidentsSelected = viewModel::onResidentsSelected,
                residentError = state.residentError,
                residents = residents,

                dueDate = state.dueDate,
                onDateChange = viewModel::onDateSelected,
                dateError = state.dateError,

                onSubmit = {
                    if (!viewModel.validateFields()) return@AssignmentFormState

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
                },
                submitButtonText = "Salvar"
            )
        )
    }
}
