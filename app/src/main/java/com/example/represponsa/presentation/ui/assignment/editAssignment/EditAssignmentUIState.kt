package com.example.represponsa.presentation.ui.assignment.editAssignment

import com.example.represponsa.data.model.User
import java.util.Date

data class EditAssignmentUiState(
    val title: String = "",
    val description: String = "",
    val selectedResidents: List<User> = emptyList(),
    val dueDate: Date = Date(),
    val titleError: String? = null,
    val residentError: String? = null,
    val dateError: String? = null
)