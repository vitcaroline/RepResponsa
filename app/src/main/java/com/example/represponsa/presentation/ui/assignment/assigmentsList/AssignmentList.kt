package com.example.represponsa.presentation.ui.assignment.assigmentsList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.represponsa.data.model.Assignment
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AssignmentList(
    assignments: List<Assignment>,
    onCompleteClicked: (Assignment) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignments) { assignment ->
            AssignmentItem(
                assignment = assignment,
                onCompleteClicked = onCompleteClicked
            )
        }
    }
}

@Composable
fun AssignmentItem(
    assignment: Assignment,
    onCompleteClicked: (Assignment) -> Unit
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(assignment.dueDate)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(text = assignment.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = assignment.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { onCompleteClicked(assignment) }) {
                    Icon(Icons.Default.AddTask,
                        contentDescription = "Concluir tarefa",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .background(Color.White)
                            .size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Designado(s): ${assignment.assignedResidentsNames.joinToString(", ")}",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Data limite: $formattedDate",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

