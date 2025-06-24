package com.example.represponsa.presentation.ui.assignment.editAssignment

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.assignment.commons.AssignmentForm
import com.example.represponsa.presentation.ui.assignment.commons.AssignmentFormState
import com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel.EditAssignmentViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditAssignmentDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAssignmentList : () -> Unit,
    viewModel: EditAssignmentViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state
    val residents by viewModel.residents

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = "Editar Tarefa",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        AssignmentForm(
            modifier = Modifier.padding(innerPadding),
            params = AssignmentFormState(
                title = state.title,
                onTitleChange = viewModel::onEditTitleChange,
                titleError = state.titleError,

                description = state.description,
                onDescriptionChange = viewModel::onEditDescriptionChange,

                selectedResidents = state.selectedResidents,
                onResidentsSelected = viewModel::onResidentsSelected,
                residentError = state.residentError,
                residents = residents,

                dueDate = state.dueDate,
                onDateChange = viewModel::onEditDueDateChange,
                dateError = state.dateError,

                onSubmit = {
                    if (!viewModel.validateFields()) return@AssignmentFormState

                    scope.launch {
                        viewModel.saveEditedAssignment(
                            onSuccess = {
                                Toast.makeText(context, "Tarefa editada com sucesso!", Toast.LENGTH_SHORT).show()
                                onNavigateToAssignmentList()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                submitButtonText = "Salvar Alterações"
            )
        )
    }
}