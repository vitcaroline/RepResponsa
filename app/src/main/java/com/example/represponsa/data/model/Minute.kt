package com.example.represponsa.data.model

import com.google.firebase.firestore.Exclude
import java.util.Date

data class Minute(
    @get:Exclude
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val date: Date = Date()
)