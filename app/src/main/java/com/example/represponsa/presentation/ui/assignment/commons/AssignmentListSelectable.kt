package com.example.represponsa.presentation.ui.assignment.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.represponsa.data.model.Assignment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AssignmentListSelectable(
    assignments: List<Assignment>,
    selectedIds: Set<String>,
    onSelectionChange: (String, Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignments, key = { it.id }) { assignment ->
            val isSelected = selectedIds.contains(assignment.id)
            AssignmentItemSelectable(
                assignment = assignment,
                isSelected = isSelected,
                onCheckedChange = { checked ->
                    onSelectionChange(assignment.id, checked)
                }
            )
        }
    }
}

@Composable
fun AssignmentItemSelectable(
    assignment: Assignment,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val formattedDate = remember(assignment.dueDate) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(assignment.dueDate)
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.surfaceBright
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = assignment.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Designado(s): ${assignment.assignedResidentsNames.joinToString(", ")}",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Data limite: $formattedDate",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun AssignmentListSelectablePreview(){
    AssignmentListSelectable(
        assignments = listOf(
            Assignment(
                id = "id1",
                title = "Limpeza da cozinha",
                description = "Organizar limpeza semanal com todos os moradores.",
                assignedResidentsNames = listOf("Vitória"),
                assignedResidentsIds = listOf("1","2"),
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 3) }.time
            ),
            Assignment(
                id = "id2",
                title = "Revisar contas",
                description = "Verificar pendências da luz e internet.",
                assignedResidentsNames = listOf("Lucas"),
                assignedResidentsIds = listOf("1","2"),
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 7) }.time
            )
        ),
        selectedIds = setOf(),
        onSelectionChange = { _, _ -> }
    )
}