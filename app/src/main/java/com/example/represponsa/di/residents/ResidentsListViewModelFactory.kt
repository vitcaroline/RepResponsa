package com.example.represponsa.di.residents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.presentation.ui.residents.viewModel.ResidentListViewModel
import com.google.firebase.auth.FirebaseAuth

object ResidentListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val userRepo = UserRepository()
        return ResidentListViewModel(userRepo, authRepo) as T
    }
}