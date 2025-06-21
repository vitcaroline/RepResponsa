package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.repository.AuthRepository
import com.example.represponsa.ui.home.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

object HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AuthRepository(FirebaseAuth.getInstance())
        return HomeViewModel(repo) as T
    }
}