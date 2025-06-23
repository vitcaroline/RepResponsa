package com.example.represponsa.data.repository

import com.example.represponsa.data.model.Minute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MinutesRepository(
    private val authRepo: AuthRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getMinutes(): List<Minute> {
        val currentUser = authRepo.getCurrentUser() ?: return emptyList()
        val snapshot = firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("minutes")
            .orderBy("date")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Minute::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun deleteMinutes(ids: List<String>) {
        val currentUser = authRepo.getCurrentUser() ?: return
        val collection = firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("minutes")

        val batch = firestore.batch()
        ids.forEach { id ->
            batch.delete(collection.document(id))
        }
        batch.commit().await()
    }

    suspend fun updateMinute(minute: Minute) {
        val currentUser = authRepo.getCurrentUser() ?: return
        firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("minutes")
            .document(minute.id)
            .set(minute)
            .await()
    }

    suspend fun createMinute(minute: Minute) {
        val currentUser = authRepo.getCurrentUser() ?: return
        firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("minutes")
            .add(minute)
            .await()
    }

    suspend fun getMinuteById(id: String): Minute? {
        val currentUser = authRepo.getCurrentUser() ?: return null
        val snapshot = firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("minutes")
            .document(id)
            .get()
            .await()

        return snapshot.toObject(Minute::class.java)?.copy(id = snapshot.id)
    }
}