package com.example.represponsa.data.model

import com.google.firebase.firestore.Exclude
import java.util.Date

data class Assignment (
    @get:Exclude
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignedResidentsIds: List<String> = emptyList(),
    val assignedResidentsNames: List<String> = emptyList(),
    val dueDate: Date = Date(),
    val completedBy: Map<String, Boolean> = emptyMap()
)