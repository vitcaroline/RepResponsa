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
import com.example.represponsa.presentation.ui.commons.validateSelectedResidents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAssignmentViewModel @Inject constructor(
    private val createAssignmentUseCase: CreateAssignmentUseCase,
    private val authRepo: AuthRepository,
    private val userRepository: UserRepository,
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

    fun onResidentsSelected(users: List<User>) {
        _state.value = _state.value.copy(
            selectedResidents = users,
            residentError = null
        )
    }

    fun onDateSelected(date: Date) {
        _state.value = _state.value.copy(dueDate = date, dateError = null)
    }

    fun validateFields(): Boolean {
        val current = _state.value

        val titleError = current.title.validateAssignmentTitle()
        val residentError = current.selectedResidents.validateSelectedResidents()
        val dateError = current.dueDate.validateDueDate()

        _state.value = current.copy(
            titleError = titleError,
            residentError = residentError,
            dateError = dateError
        )

        return titleError == null && residentError == null && dateError == null
    }

    private fun fetchResidents() {
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

        if (currentUser == null) {
            onError("Erro ao carregar dados.")
            return
        }

        val assignedIds = stateValue.selectedResidents.map { it.uid }
        val assignedNames = stateValue.selectedResidents.map { it.firstName }

        val assignment = Assignment(
            title = stateValue.title,
            description = stateValue.description,
            dueDate = stateValue.dueDate,
            assignedResidentsIds = assignedIds,
            assignedResidentsNames = assignedNames
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