package com.example.represponsa.di.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.presentation.ui.home.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

object HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AuthRepository(FirebaseAuth.getInstance())
        return HomeViewModel(repo) as T
    }
}