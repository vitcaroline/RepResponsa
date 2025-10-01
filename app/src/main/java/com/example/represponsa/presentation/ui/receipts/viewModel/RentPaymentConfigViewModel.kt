package com.example.represponsa.presentation.ui.receipts.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.domain.useCases.SaveRentPaymentConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentPaymentConfigViewModel @Inject constructor(
    private val saveConfigUseCase: SaveRentPaymentConfigUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentPaymentConfigUiState())
    val uiState: StateFlow<RentPaymentConfigUiState> = _uiState

    fun onDayChange(newDay: Int) {
        _uiState.value = _uiState.value.copy(day = newDay)
    }

    fun onFixedChange(newValue: Boolean) {
        _uiState.value = _uiState.value.copy(isFixed = newValue)
    }

    fun saveConfig(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value
        val config = RentPaymentConfig(day = state.day, isFixed = state.isFixed)

        viewModelScope.launch {
            try {
                saveConfigUseCase(config)
                onSuccess()
            }
            catch (e: Exception) {
                onError(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

data class RentPaymentConfigUiState(
    val day: Int = 1,
    val isFixed: Boolean = true
)