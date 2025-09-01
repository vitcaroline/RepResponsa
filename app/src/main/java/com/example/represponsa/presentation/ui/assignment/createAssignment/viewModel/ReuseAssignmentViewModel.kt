package com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.repository.AssignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReuseAssignmentViewModel @Inject constructor(
    private val repository: AssignmentRepository
) : ViewModel() {

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments

    init {
        fetchAssignments()
    }

    private fun fetchAssignments() {
        viewModelScope.launch {
            try {
                val result = repository.getAssignments()
                _assignments.value = result
            } catch (e: Exception) {
            }
        }
    }
}
