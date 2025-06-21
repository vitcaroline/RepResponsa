package com.example.represponsa.model

import com.google.firebase.firestore.Exclude
import java.util.Date

data class Assignment (
    @get:Exclude
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignedResidentId: String = "",
    val assignedResidentName: String = "",
    val dueDate: Date = Date()
)