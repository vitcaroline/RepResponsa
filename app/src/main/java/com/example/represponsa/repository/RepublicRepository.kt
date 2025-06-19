package com.example.represponsa.repository

import com.example.represponsa.model.Republic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RepublicRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun createRepublic(republic: Republic): Result<Unit> {
        return try {
            val docRef = firestore.collection("republics").document()
            val republicToSave = republic.copy(id = docRef.id)
            docRef.set(republicToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllRepublics(onResult: (Result<List<Republic>>) -> Unit) {
        firestore.collection("republics")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(Republic::class.java) }
                onResult(Result.success(list))
            }
            .addOnFailureListener { e ->
                onResult(Result.failure(e))
            }
    }
}