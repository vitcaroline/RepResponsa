package com.example.represponsa.presentation.ui.minutes.minutesList.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Minute
import com.example.represponsa.domain.useCases.GetMinutesUseCase
import kotlinx.coroutines.launch

class MinutesListViewModel(
    private val getMinutesUseCase: GetMinutesUseCase
) : ViewModel() {

    private val _minutes = mutableStateOf<List<Minute>>(emptyList())
    val minutes: State<List<Minute>> = _minutes

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchMinutes()
    }

    fun fetchMinutes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _minutes.value = getMinutesUseCase()
            } catch (e: Exception) {
                _minutes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}