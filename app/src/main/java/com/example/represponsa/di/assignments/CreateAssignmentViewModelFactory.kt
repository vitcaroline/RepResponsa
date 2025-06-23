package com.example.represponsa.di.assignments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.CreateAssignmentUseCase
import com.example.represponsa.presentation.ui.assignment.createAssignment.viewModel.CreateAssignmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CreateAssignmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val firestore = FirebaseFirestore.getInstance()

        val assignmentRepo = AssignmentRepository(authRepo, firestore)
        val userRepo = UserRepository(firestore)

        val createAssignmentUseCase = CreateAssignmentUseCase(assignmentRepo)

        return CreateAssignmentViewModel(
            createAssignmentUseCase = createAssignmentUseCase,
            authRepo = authRepo,
            userRepository = userRepo
        ) as T
    }
}
