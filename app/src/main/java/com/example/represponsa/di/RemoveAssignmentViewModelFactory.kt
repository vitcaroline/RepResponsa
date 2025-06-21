package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.DeleteAssignmentsUseCase
import com.example.represponsa.domain.useCases.GetAssignmentsUseCase
import com.example.represponsa.presentation.ui.assignment.removeAssignment.viewModel.RemoveAssignmentViewModel
import com.google.firebase.auth.FirebaseAuth

object RemoveAssignmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val assignmentRepo = AssignmentRepository(authRepo)
        val getAssignmentsUseCase = GetAssignmentsUseCase(assignmentRepo)
        val deleteAssignmentsUseCase = DeleteAssignmentsUseCase(assignmentRepo)

        return RemoveAssignmentViewModel(deleteAssignmentsUseCase, getAssignmentsUseCase) as T
    }
}