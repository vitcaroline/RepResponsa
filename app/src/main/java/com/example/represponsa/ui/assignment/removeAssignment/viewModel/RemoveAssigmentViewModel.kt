package com.example.represponsa.ui.assignment.removeAssignment.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.model.Assignment
import com.example.represponsa.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RemoveAssignmentViewModel(
    private val authRepo: AuthRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _assignments = mutableStateOf<List<Assignment>>(emptyList())
    val assignments: State<List<Assignment>> = _assignments

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchAssignments()
    }

    fun fetchAssignments() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUser = authRepo.getCurrentUser()
            if (currentUser == null) {
                _isLoading.value = false
                _assignments.value = emptyList()
                return@launch
            }
            try {
                val snapshot = firestore.collection("republics")
                    .document(currentUser.republicId)
                    .collection("assignments")
                    .orderBy("dueDate")
                    .get()
                    .await()

                _assignments.value = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Assignment::class.java)?.copy(id = doc.id)
                }
            } catch (e: Exception) {
                _assignments.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAssignments(ids: List<String>, onComplete: () -> Unit) {
        viewModelScope.launch {
            val currentUser = authRepo.getCurrentUser() ?: return@launch

            val batch = firestore.batch()
            val collection = firestore.collection("republics")
                .document(currentUser.republicId)
                .collection("assignments")

            ids.forEach { id ->
                val docRef = collection.document(id)
                batch.delete(docRef)
            }

            try {
                batch.commit().await()
                fetchAssignments()
                onComplete()
            } catch (e: Exception) {
            }
        }
    }
}