package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.repository.AuthRepository
import com.example.represponsa.repository.RepublicRepository
import com.example.represponsa.ui.registerUser.viewModel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object RegisterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AuthRepository(FirebaseAuth.getInstance())
        val republicRepo = RepublicRepository(FirebaseFirestore.getInstance())
        return RegisterViewModel(repo, republicRepo ) as T
    }
}