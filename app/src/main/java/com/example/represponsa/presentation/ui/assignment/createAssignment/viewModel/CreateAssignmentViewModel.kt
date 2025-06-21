package com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel

import androidx.lifecycle.ViewModel
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import java.util.Date
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.CreateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.createAssignment.CreateAssignmentUiState
import com.example.represponsa.presentation.ui.commons.validateAssignmentTitle
import com.example.represponsa.presentation.ui.commons.validateDueDate
import com.example.represponsa.presentation.ui.commons.validateSelectedResident
import kotlinx.coroutines.launch

class CreateAssignmentViewModel(
    private val createAssignmentUseCase: CreateAssignmentUseCase,
    private val authRepo: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = mutableStateOf(CreateAssignmentUiState())
    val state: State<CreateAssignmentUiState> = _state

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    init {
        fetchResidents()
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

    fun fetchResidents() {
        viewModelScope.launch {
            try {
                val currentUser = authRepo.getCurrentUser() ?: return@launch
                _residents.value = userRepository.getResidentsByRepublic(currentUser.republicId)
            } catch (_: Exception) {
                _residents.value = emptyList()
            }
        }
    }

    suspend fun createAssignment(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        if (!validateFields()) return

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

        viewModelScope.launch {
            try {
                createAssignmentUseCase(assignment)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar tarefa")
            }
        }
    }
}