package com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.UpdateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentUiState
import com.example.represponsa.presentation.ui.commons.validateAssignmentTitle
import com.example.represponsa.presentation.ui.commons.validateDueDate
import com.example.represponsa.presentation.ui.commons.validateSelectedResident
import kotlinx.coroutines.launch
import java.util.Date

class EditAssignmentViewModel(
    private val updateAssignmentUseCase: UpdateAssignmentUseCase,
    private val authRepo: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var originalAssignmentId: String? = null

    private val _state = mutableStateOf(EditAssignmentUiState())
    val state: State<EditAssignmentUiState> = _state

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    fun startEditing(assignment: Assignment) {
        originalAssignmentId = assignment.id

        viewModelScope.launch {
            fetchResidents()
            val currentResidents = _residents.value
            val selectedUser = currentResidents.find { it.uid == assignment.assignedResidentId }

            _state.value = EditAssignmentUiState(
                title = assignment.title,
                description = assignment.description,
                dueDate = assignment.dueDate,
                selectedResident = selectedUser
            )
        }
    }

    fun onEditTitleChange(newTitle: String) {
        _state.value = _state.value.copy(title = newTitle, titleError = null)
    }

    fun onEditDescriptionChange(newDescription: String) {
        _state.value = _state.value.copy(description = newDescription)
    }

    fun onEditDueDateChange(newDate: Date) {
        _state.value = _state.value.copy(dueDate = newDate, dateError = null)
    }

    fun onResidentSelected(user: User) {
        _state.value = _state.value.copy(selectedResident = user, residentError = null)
    }

    private suspend fun fetchResidents() {
        val currentUser = authRepo.getCurrentUser() ?: return
        val residents = userRepository.getResidentsByRepublic(currentUser.republicId)
        _residents.value = residents
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

    fun saveEditedAssignment(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val stateValue = _state.value
        val resident = stateValue.selectedResident

        if (!validateFields() || originalAssignmentId == null || resident == null) return

        val updatedAssignment = Assignment(
            id = originalAssignmentId!!,
            title = stateValue.title,
            description = stateValue.description,
            dueDate = stateValue.dueDate,
            assignedResidentId = resident.uid,
            assignedResidentName = resident.firstName
        )

        viewModelScope.launch {
            try {
                updateAssignmentUseCase(updatedAssignment)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao atualizar tarefa")
            }
        }
    }
}