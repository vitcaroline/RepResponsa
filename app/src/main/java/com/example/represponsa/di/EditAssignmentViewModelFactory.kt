package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.GetAssignmentByIdUseCase
import com.example.represponsa.domain.useCases.UpdateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.editAssignment.viewModel.EditAssignmentViewModel
import com.google.firebase.auth.FirebaseAuth

class EditAssignmentViewModelFactory(
    private val assignmentId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val assignmentRepository = AssignmentRepository(authRepo)
        val userRepository = UserRepository()

        return EditAssignmentViewModel(
            getAssignmentByIdUseCase = GetAssignmentByIdUseCase(assignmentRepository),
            updateAssignmentUseCase = UpdateAssignmentUseCase(assignmentRepository),
            authRepo = authRepo,
            userRepository = userRepository,
            assignmentId = assignmentId
        ) as T
    }
}