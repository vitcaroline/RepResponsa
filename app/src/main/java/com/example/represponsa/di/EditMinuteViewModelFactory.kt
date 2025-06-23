package com.example.represponsa.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.MinutesRepository
import com.example.represponsa.domain.useCases.GetMinuteByIdUseCase
import com.example.represponsa.domain.useCases.UpdateMinuteUseCase
import com.example.represponsa.presentation.ui.minutes.editMinute.viewModel.EditMinuteViewModel

class EditMinuteViewModelFactory(
    private val minuteId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = MinutesRepository(AuthRepository())
        return EditMinuteViewModel(
            getMinuteByIdUseCase = GetMinuteByIdUseCase(repo),
            updateMinuteUseCase = UpdateMinuteUseCase(repo),
            savedStateHandle = SavedStateHandle(mapOf("minuteId" to minuteId))
        ) as T
    }
}