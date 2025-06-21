package com.example.represponsa.presentation.ui.assignment.createAssignment

import com.example.represponsa.data.model.User
import java.util.Date

data class CreateAssignmentUiState(
    val title: String = "",
    val description: String = "",
    val selectedResident: User? = null,
    val dueDate: Date = Date(),
    val titleError: String? = null,
    val residentError: String? = null,
    val dateError: String? = null
)