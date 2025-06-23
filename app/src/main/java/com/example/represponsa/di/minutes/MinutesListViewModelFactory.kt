package com.example.represponsa.di.minutes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.MinutesRepository
import com.example.represponsa.domain.useCases.GetMinutesUseCase
import com.example.represponsa.presentation.ui.minutes.minutesList.viewModel.MinutesListViewModel
import com.google.firebase.auth.FirebaseAuth

object MinutesListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authRepo = AuthRepository(FirebaseAuth.getInstance())
        val minutesRepo = MinutesRepository(authRepo)
        val getMinutesUseCase = GetMinutesUseCase(minutesRepo)
        return MinutesListViewModel(getMinutesUseCase) as T
    }
}