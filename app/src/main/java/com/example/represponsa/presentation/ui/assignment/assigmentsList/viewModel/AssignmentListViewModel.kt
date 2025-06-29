package com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.represponsa.data.model.Assignment
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.represponsa.domain.useCases.GetAssignmentsUseCase
import com.example.represponsa.domain.useCases.GetFilteredAssignmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentListViewModel @Inject constructor(
    private val getFilteredAssignmentsUseCase: GetFilteredAssignmentsUseCase
) : ViewModel() {

    private val _assignments = mutableStateOf<List<Assignment>>(emptyList())
    val assignments: State<List<Assignment>> = _assignments

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _showOnlyMyAssignments = mutableStateOf(false)
    val showOnlyMyAssignments: State<Boolean> = _showOnlyMyAssignments
    init {
        fetchAssignments()
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
                _assignments.value = getFilteredAssignmentsUseCase(_showOnlyMyAssignments.value)
            } catch (e: Exception) {
                _assignments.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
