package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.ui.registerRepublic.viewModel.RegisterRepublicViewModel
import com.example.represponsa.repository.RepublicRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object RegisterRepublicViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = RepublicRepository(Firebase.firestore)
        return RegisterRepublicViewModel(repository) as T
    }
}