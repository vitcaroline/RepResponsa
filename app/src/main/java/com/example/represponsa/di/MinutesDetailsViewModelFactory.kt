package com.example.represponsa.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.MinutesRepository
import com.example.represponsa.domain.useCases.DeleteMinuteUseCase
import com.example.represponsa.domain.useCases.GetMinuteByIdUseCase
import com.example.represponsa.presentation.ui.minutes.minutesList.viewModel.MinuteDetailsViewModel

class MinuteDetailsViewModelFactory(
    private val minuteId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = MinutesRepository(AuthRepository())
        return MinuteDetailsViewModel(
            getMinuteByIdUseCase = GetMinuteByIdUseCase(repository),
            savedStateHandle = SavedStateHandle(mapOf("minuteId" to minuteId)),
            deleteMinuteUseCase = DeleteMinuteUseCase(repository)
        ) as T
    }
}