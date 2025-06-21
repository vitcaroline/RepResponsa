package com.example.represponsa.data.repository

import com.example.represponsa.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getResidentsByRepublic(republicId: String): List<User> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("republicId", republicId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(User::class.java) }
    }
}