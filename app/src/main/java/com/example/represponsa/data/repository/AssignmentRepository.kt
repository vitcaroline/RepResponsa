package com.example.represponsa.data.repository

import com.example.represponsa.data.model.Assignment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssignmentRepository(
    private val authRepo: AuthRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getAssignments(): List<Assignment> {
        val currentUser = authRepo.getCurrentUser() ?: return emptyList()
        val snapshot = firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("assignments")
            .orderBy("dueDate")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Assignment::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun deleteAssignments(ids: List<String>) {
        val currentUser = authRepo.getCurrentUser() ?: return
        val collection = firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("assignments")

        val batch = firestore.batch()
        ids.forEach { id ->
            batch.delete(collection.document(id))
        }
        batch.commit().await()
    }

    suspend fun updateAssignment(assignment: Assignment) {
        val currentUser = authRepo.getCurrentUser() ?: return
        firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("assignments")
            .document(assignment.id)
            .set(assignment)
            .await()
    }

    suspend fun createAssignment(assignment: Assignment) {
        val currentUser = authRepo.getCurrentUser() ?: return
        firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("assignments")
            .add(assignment)
            .await()
    }
}