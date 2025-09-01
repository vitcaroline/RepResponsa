package com.example.represponsa.presentation.ui.assignment.createAssignment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel.ReuseAssignmentViewModel
import com.example.represponsa.presentation.ui.commons.TopBar

@Composable
fun ReuseAssignmentScreen(
    onSelectAssignment: (Assignment) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ReuseAssignmentViewModel = hiltViewModel()
) {
    val assignments by viewModel.assignments.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Selecionar tarefa existente",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma tarefa disponível.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(assignments) { assignment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onSelectAssignment(assignment) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = assignment.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = assignment.description)
                        }
                    }
                }
            }
        }
    }
}
