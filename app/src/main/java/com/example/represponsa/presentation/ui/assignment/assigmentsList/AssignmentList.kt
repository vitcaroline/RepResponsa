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
                assignedResidents = assignment.assignedResidentsNames,
                dueDate = assignment.dueDate
            )
        }
    }
}

@Composable
fun AssignmentItem(
    title: String,
    description: String,
    assignedResidents: List<String>,
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
            Text(
                text = "Designado(s): ${assignedResidents.joinToString(", ")}",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Data limite: $formattedDate",
                style = MaterialTheme.typography.labelLarge
                )
            }
        }
}

@Preview
@Composable
fun AssignmentListPreview(){
    AssignmentList(
        assignments = listOf(
            Assignment(
                id = "id1",
                title = "Limpeza da cozinha",
                description = "Organizar limpeza semanal com todos os moradores.",
                assignedResidentsNames = listOf("Vitória","Beto"),
                assignedResidentsIds = listOf("1,2"),
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 3) }.time
            ),
            Assignment(
                id = "id2",
                title = "Revisar contas",
                description = "Verificar pendências da luz e internet.",
                assignedResidentsNames = listOf("Lucas","Beto"),
                assignedResidentsIds = listOf("1,2"),
                dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 7) }.time
            )
        )
    )
}

