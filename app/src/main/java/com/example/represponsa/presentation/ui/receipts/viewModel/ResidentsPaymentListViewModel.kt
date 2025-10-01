package com.example.represponsa.presentation.ui.receipts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.CheckUserPaymentStatusUseCase
import com.example.represponsa.domain.useCases.ReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResidentsPaymentListViewModel @Inject constructor(
    private val saveConfigUseCase: ReceiptsUseCase,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val checkUserPaymentStatusUseCase: CheckUserPaymentStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentPaymentConfigUiState())
    val uiState: StateFlow<RentPaymentConfigUiState> = _uiState

    init {
        checkAccessAndLoadResidents()
    }

    private fun checkAccessAndLoadResidents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val user = authRepository.getCurrentUser()
            if (user == null) {
                _uiState.value = _uiState.value.copy(isAuthorized = false, isLoading = false)
                return@launch
            }

            val roles = user.role.split(",").map { it.trim() }
            val isAuthorized = roles.contains(RolesEnum.FINANCEIRO.name)

            if (!isAuthorized) {
                _uiState.value = _uiState.value.copy(
                    isAuthorized = false,
                    user = user,
                    isLoading = false
                )
                return@launch
            }

            try {
                val residents = userRepository.getResidentsByRepublic(user.republicId)

                val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
                val currentMonth = sdf.format(System.currentTimeMillis())

                val updatedList = residents.map { resident ->
                    val hasPaid = checkUserPaymentStatusUseCase(
                        resident.uid,
                        user.republicId,
                        currentMonth
                    )
                    ResidentPaymentStatus(resident, hasPaid)
                }

                _uiState.value = _uiState.value.copy(
                    isAuthorized = true,
                    residentsWithStatus = updatedList,
                    user = user,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    residentsWithStatus = emptyList(),
                    isAuthorized = true,
                    user = user,
                    isLoading = false
                )
            }
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
            } catch (e: Exception) {
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
    val isLoading: Boolean = true,
    val residentsWithStatus: List<ResidentPaymentStatus> = emptyList()
)

data class ResidentPaymentStatus(
    val resident: User,
    val hasPaid: Boolean
)