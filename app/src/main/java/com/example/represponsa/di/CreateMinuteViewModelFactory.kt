package com.example.represponsa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.MinutesRepository
import com.example.represponsa.domain.useCases.CreateMinuteUseCase
import com.example.represponsa.presentation.ui.minutes.createMinute.viewModel.CreateMinuteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CreateMinuteViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val firestore = FirebaseFirestore.getInstance()

        val minuteRepo = MinutesRepository(authRepo, firestore)
        val createMinuteUseCase = CreateMinuteUseCase(minuteRepo)

        return CreateMinuteViewModel(
            createMinuteUseCase = createMinuteUseCase,
            authRepo = authRepo,
        ) as T
    }
}