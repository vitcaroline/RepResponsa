package com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.represponsa.data.model.Assignment
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.CompleteAssignmentUseCase
import com.example.represponsa.domain.useCases.GetFilteredAssignmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getFilteredAssignmentsUseCase: GetFilteredAssignmentsUseCase,
    private val completeAssignmentUseCase: CompleteAssignmentUseCase
) : ViewModel() {

    private val _assignments = mutableStateOf<List<Assignment>>(emptyList())
    val assignments: State<List<Assignment>> = _assignments

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _showOnlyMyAssignments = mutableStateOf(false)
    val showOnlyMyAssignments: State<Boolean> = _showOnlyMyAssignments

    private val _canManageAssignments = mutableStateOf(false)
    val canManageAssignments: State<Boolean> = _canManageAssignments


    private val _showCompletionDialog = mutableStateOf<Assignment?>(null)
    val showCompletionDialog: State<Assignment?> = _showCompletionDialog

    init {
        viewModelScope.launch {
            loadUserRole()
            fetchAssignments()
        }
    }

    private suspend fun loadUserRole() {
        try {
            val currentUser = authRepository.getCurrentUser()
            _canManageAssignments.value = currentUser?.role?.contains("Faxina", ignoreCase = true) == true
        } catch (e: Exception) {
            _canManageAssignments.value = false
        }
    }

    fun setFilter(onlyMine: Boolean) {
        if (_showOnlyMyAssignments.value != onlyMine) {
            _showOnlyMyAssignments.value = onlyMine
            fetchAssignments()
        }
    }

    fun fetchAssignments() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allAssignments = getFilteredAssignmentsUseCase(_showOnlyMyAssignments.value)

                _assignments.value = allAssignments.filter { assignment ->
                    assignment.assignedResidentsIds.any { id ->
                        assignment.completedBy[id] != true
                    }
                }
            } catch (e: Exception) {
                _assignments.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun showCompletionConfirmation(assignment: Assignment) {
        _showCompletionDialog.value = assignment
    }

    fun dismissCompletionDialog() {
        _showCompletionDialog.value = null
    }

    fun completeAssignment(assignment: Assignment) {
        viewModelScope.launch {
            try {
                val currentUserId = authRepository.getCurrentUser()?.uid ?: return@launch
                completeAssignmentUseCase(assignment, currentUserId)
                fetchAssignments()
            } catch (e: Exception) {
                Log.e("AssignmentVM", "Erro ao completar tarefa: ${e.message}")
            } finally {
                _showCompletionDialog.value = null
            }
        }
    }
}
