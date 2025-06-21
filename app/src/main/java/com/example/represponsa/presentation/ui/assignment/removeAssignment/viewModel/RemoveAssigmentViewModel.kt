package com.example.represponsa.presentation.ui.assignment.removeAssignment.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.domain.useCases.DeleteAssignmentsUseCase
import com.example.represponsa.domain.useCases.GetAssignmentsUseCase
import kotlinx.coroutines.launch

class RemoveAssignmentViewModel(
    private val deleteAssignmentsUseCase: DeleteAssignmentsUseCase,
    private val getAssignmentsUseCase: GetAssignmentsUseCase
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
            try {
                _assignments.value = getAssignmentsUseCase()
            } catch (e: Exception) {
                _assignments.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAssignments(ids: List<String>, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                deleteAssignmentsUseCase(ids)
                fetchAssignments()
                onComplete()
            } catch (_: Exception) { }
        }
    }
}