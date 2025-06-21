package com.example.represponsa.ui.assignment.createAssignment.viewModel

import androidx.lifecycle.ViewModel
import com.example.represponsa.model.Assignment
import com.example.represponsa.model.User
import com.example.represponsa.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.represponsa.ui.assignment.createAssignment.CreateAssignmentUiState
import com.example.represponsa.ui.commons.validateAssignmentTitle
import com.example.represponsa.ui.commons.validateDueDate
import com.example.represponsa.ui.commons.validateSelectedResident
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CreateAssignmentViewModel(
    private val authRepo: AuthRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _state = mutableStateOf(CreateAssignmentUiState())
    val state: State<CreateAssignmentUiState> = _state

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    init {
        viewModelScope.launch {
            fetchResidents()
        }
    }

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value, titleError = null)
    }

    fun onDescriptionChange(value: String) {
        _state.value = _state.value.copy(description = value)
    }

    fun onResidentSelected(user: User) {
        _state.value = _state.value.copy(selectedResident = user, residentError = null)
    }

    fun onDateSelected(date: Date) {
        _state.value = _state.value.copy(dueDate = date, dateError = null)
    }

    fun validateFields(): Boolean {
        val current = _state.value

        val titleError = current.title.validateAssignmentTitle()
        val residentError = current.selectedResident.validateSelectedResident()
        val dateError = current.dueDate.validateDueDate()

        _state.value = current.copy(
            titleError = titleError,
            residentError = residentError,
            dateError = dateError
        )

        return titleError == null && residentError == null && dateError == null
    }

    suspend fun fetchResidents() {
        val currentUser = authRepo.getCurrentUser() ?: return
        val republicId = currentUser.republicId

        val snapshot = firestore.collection("users")
            .whereEqualTo("republicId", republicId)
            .get()
            .await()

        _residents.value = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
    }

    suspend fun createAssignment(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val currentUser = authRepo.getCurrentUser()
        val stateValue = _state.value
        val resident = stateValue.selectedResident

        if (currentUser == null || resident == null) {
            onError("Erro ao carregar dados.")
            return
        }

        val assignment = Assignment(
            title = stateValue.title,
            description = stateValue.description,
            dueDate = stateValue.dueDate,
            assignedResidentId = resident.uid,
            assignedResidentName = resident.firstName
        )

        firestore.collection("republics")
            .document(currentUser.republicId)
            .collection("assignments")
            .add(assignment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Erro desconhecido") }
    }
}