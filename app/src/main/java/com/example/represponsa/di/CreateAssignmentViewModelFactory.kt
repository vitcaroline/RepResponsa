package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.repository.AuthRepository
import com.example.represponsa.ui.assignment.createAssignment.viewModel.CreateAssignmentViewModel

object CreateAssignmentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository()
        return CreateAssignmentViewModel(authRepo) as T
    }
}
