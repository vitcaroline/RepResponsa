package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.repository.AuthRepository
import com.example.represponsa.ui.login.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

object LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val auth = FirebaseAuth.getInstance()
        return LoginViewModel(AuthRepository(auth)) as T
    }
}