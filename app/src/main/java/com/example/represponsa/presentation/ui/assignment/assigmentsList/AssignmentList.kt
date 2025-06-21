package com.example.represponsa.presentation.ui.assignment.assigmentsList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.represponsa.data.model.Assignment
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AssignmentList(assignments: List<Assignment>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignments) { assignment ->
            AssignmentItem(
                title = assignment.title,
                description = assignment.description,
                assignedResident = assignment.assignedResidentName,
                dueDate = assignment.dueDate
            )
        }
    }
}

@Composable
fun AssignmentItem(
    title: String,
    description: String,
    assignedResident: String,
    dueDate: Date,
    modifier: Modifier = Modifier
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dueDate)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Designado: $assignedResident",
                    style = MaterialTheme.typography.labelLarge
                )
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
fun AssignmentListPreview(){
    AssignmentList(
        assignments = listOf(
            Assignment(
                title = "Limpeza da cozinha",
                description = "Organizar limpeza semanal com todos os moradores.",
                assignedResidentName = "Vitória",
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 3) }.time
            ),
            Assignment(
                title = "Revisar contas",
                description = "Verificar pendências da luz e internet.",
                assignedResidentName = "Lucas",
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 7) }.time
            )
        )
    )
}

