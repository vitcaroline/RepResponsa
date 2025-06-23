package com.example.represponsa.di.assignments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.GetAssignmentsUseCase
import com.example.represponsa.presentation.ui.assignment.assigmentsList.viewModel.AssignmentListViewModel
import com.google.firebase.auth.FirebaseAuth

object AssignmentListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val assignmentRepo = AssignmentRepository(authRepo)
        val getAssignmentsUseCase = GetAssignmentsUseCase(assignmentRepo)
        return AssignmentListViewModel(getAssignmentsUseCase) as T
    }
}
