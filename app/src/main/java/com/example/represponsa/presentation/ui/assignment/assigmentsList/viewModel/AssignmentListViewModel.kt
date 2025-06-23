package com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.represponsa.data.model.Assignment
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.represponsa.domain.useCases.GetAssignmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentListViewModel @Inject constructor(
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
}
