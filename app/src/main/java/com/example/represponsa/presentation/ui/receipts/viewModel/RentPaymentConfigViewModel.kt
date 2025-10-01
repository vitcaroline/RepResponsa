package com.example.represponsa.presentation.ui.receipts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.SaveRentPaymentConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentPaymentConfigViewModel @Inject constructor(
    private val saveConfigUseCase: SaveRentPaymentConfigUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentPaymentConfigUiState())
    val uiState: StateFlow<RentPaymentConfigUiState> = _uiState

    init {
        checkAccess()
    }

    private fun checkAccess() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()

            val roles = user?.role?.split(",")?.map { it.trim() } ?: emptyList()

            val isAuthorized = roles.contains(RolesEnum.FINANCEIRO.name)

            _uiState.value = _uiState.value.copy(
                isAuthorized = isAuthorized,
                user = user,
                isLoading = false
            )
        }
    }

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
    val isAuthorized: Boolean = false,
    val day: Int = 1,
    val isFixed: Boolean = true,
    val user: User? = null,
    val isLoading: Boolean = true
)