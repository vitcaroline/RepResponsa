package com.example.represponsa.ui.assignment.editAssignment

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.AssignmentViewModelFactory
import com.example.represponsa.ui.assignment.assigmentsList.viewModel.AssignmentViewModel
import com.example.represponsa.ui.commons.DatePickerButton
import com.example.represponsa.ui.commons.TopBar

@Composable
fun EditAssignmentDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AssignmentViewModel = viewModel(factory = AssignmentViewModelFactory)
) {
    val assignment = viewModel.editingAssignment.value ?: return
    val context = LocalContext.current

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
                value = assignment.title,
                onValueChange = viewModel::onEditTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = assignment.description,
                onValueChange = viewModel::onEditDescriptionChange,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            DatePickerButton(
                selectedDate = assignment.dueDate,
                onDateSelected = viewModel::onEditDueDateChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveEditedAssignment(
                        onSuccess = {
                            Toast.makeText(context, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }
    }
}