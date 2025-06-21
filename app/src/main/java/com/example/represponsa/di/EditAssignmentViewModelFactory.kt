package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.UpdateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel.EditAssignmentViewModel
import com.google.firebase.auth.FirebaseAuth

object EditAssignmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val assignmentRepo = AssignmentRepository(authRepo)
        val userRepository = UserRepository()
        val updateAssignmentUseCase = UpdateAssignmentUseCase(assignmentRepo)

        return EditAssignmentViewModel(updateAssignmentUseCase, authRepo, userRepository) as T
    }
}