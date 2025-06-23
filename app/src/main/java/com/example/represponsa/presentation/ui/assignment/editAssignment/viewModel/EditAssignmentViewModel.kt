package com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.GetAssignmentByIdUseCase
import com.example.represponsa.domain.useCases.UpdateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.editAssignment.EditAssignmentUiState
import com.example.represponsa.presentation.ui.commons.validateAssignmentTitle
import com.example.represponsa.presentation.ui.commons.validateDueDate
import com.example.represponsa.presentation.ui.commons.validateSelectedResidents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditAssignmentViewModel @Inject constructor(
    private val getAssignmentByIdUseCase: GetAssignmentByIdUseCase,
    private val updateAssignmentUseCase: UpdateAssignmentUseCase,
    private val authRepo: AuthRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val assignmentId = savedStateHandle.get<String>("assignmentId") ?: ""

    private val _state = mutableStateOf(EditAssignmentUiState())
    val state: State<EditAssignmentUiState> = _state

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    init {
        fetchAssignmentAndResidents()
    }

    private fun fetchAssignmentAndResidents() {
        viewModelScope.launch {
            val assignment = getAssignmentByIdUseCase(assignmentId)
            if (assignment != null) {
                val currentUser = authRepo.getCurrentUser() ?: return@launch
                val resList = userRepository.getResidentsByRepublic(currentUser.republicId)
                _residents.value = resList

                val selected = resList.filter { it.uid in assignment.assignedResidentsIds }

                _state.value = EditAssignmentUiState(
                    title = assignment.title,
                    description = assignment.description,
                    dueDate = assignment.dueDate,
                    selectedResidents = selected
                )
            }
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

    fun onResidentsSelected(users: List<User>) {
        _state.value = _state.value.copy(selectedResidents = users, residentError = null)
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

    fun saveEditedAssignment(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val stateValue = _state.value
        val selectedResidents = stateValue.selectedResidents

        if (!validateFields() || selectedResidents.isEmpty()) return

        val assignedIds = stateValue.selectedResidents.map { it.uid }
        val assignedNames = stateValue.selectedResidents.map { it.firstName }

        val updatedAssignment = Assignment(
            id = assignmentId,
            title = stateValue.title,
            description = stateValue.description,
            dueDate = stateValue.dueDate,
            assignedResidentsIds = assignedIds,
            assignedResidentsNames = assignedNames
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