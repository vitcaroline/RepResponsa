package com.example.represponsa.presentation.ui.assignment.commons

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.represponsa.data.model.User
import com.example.represponsa.presentation.ui.commons.DatePickerButton
import java.util.Date

data class AssignmentFormState(
    val title: String,
    val onTitleChange: (String) -> Unit,
    val titleError: String? = null,

    val description: String,
    val onDescriptionChange: (String) -> Unit,

    val selectedResident: User?,
    val onResidentSelected: (User) -> Unit,
    val residentError: String? = null,
    val residents: List<User> = emptyList(),

    val dueDate: Date,
    val onDateChange: (Date) -> Unit,
    val dateError: String? = null,

    val onSubmit: () -> Unit,
    val submitButtonText: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentForm(
    modifier: Modifier = Modifier,
    params: AssignmentFormState,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(24.dp)) {
        OutlinedTextField(
            value = params.title,
            onValueChange = params.onTitleChange,
            label = { Text("Título") },
            isError = params.titleError != null,
            supportingText = {
                params.titleError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = params.description,
            onValueChange = params.onDescriptionChange,
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = params.selectedResident?.firstName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Morador Responsável") },
                isError = params.residentError != null,
                supportingText = {
                    params.residentError?.let {
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
                params.residents.forEach { user ->
                    DropdownMenuItem(
                        text = { Text(user.firstName) },
                        onClick = {
                            params.onResidentSelected(user)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerButton(
            selectedDate = params.dueDate,
            onDateSelected = params.onDateChange
        )
        params.dateError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = params.onSubmit, modifier = Modifier.fillMaxWidth()) {
            Text(params.submitButtonText)
        }
    }
}