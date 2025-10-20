package com.example.represponsa.presentation.ui.receipts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.core.notification.RentNotificationManager
import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import com.example.represponsa.domain.useCases.CheckUserPaymentStatusUseCase
import com.example.represponsa.domain.useCases.GetRepublicByIdUseCase
import com.example.represponsa.domain.useCases.ReceiptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ResidentsPaymentListViewModel @Inject constructor(
    private val saveConfigUseCase: ReceiptsUseCase,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val getRepublicByIdUseCase: GetRepublicByIdUseCase,
    private val checkUserPaymentStatusUseCase: CheckUserPaymentStatusUseCase,
    private val rentNotificationManager: RentNotificationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentPaymentConfigUiState())
    val uiState: StateFlow<RentPaymentConfigUiState> = _uiState

    private var currentNotificationId: Int? = null

    init {
        loadUserAndResidents()
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
                currentNotificationId?.let { rentNotificationManager.cancelRentNotification(it) }

                state.user?.let { user ->
                    schedulePaymentNotificationForUser(user, state.day)
                }

                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun loadUserAndResidents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = authRepository.getCurrentUser()

            if (user == null) {
                _uiState.value = _uiState.value.copy(isAuthorized = false, isLoading = false)
                return@launch
            }

            val roles = getUserRoles(user)
            val isFinanceiro = RolesEnum.FINANCEIRO.name in roles

            if (!isFinanceiro) {
                scheduleNotificationForNonFinanceUser(user)
            }

            loadResidents(user, roles.contains(RolesEnum.FINANCEIRO.name))
        }
    }

    private suspend fun loadResidents(user: User, isFinanceiro: Boolean) {
        try {
            val residents = userRepository.getResidentsByRepublic(user.republicId)
            val currentMonth = getCurrentMonthName()

            val updatedList = residents.map { resident ->
                val monthKey = getCurrentMonthKey()
                val hasPaid = checkUserPaymentStatusUseCase(resident.uid, user.republicId, monthKey)
                ResidentPaymentStatus(resident, hasPaid)
            }

            _uiState.value = _uiState.value.copy(
                isAuthorized = true,
                residentsWithStatus = updatedList,
                user = user,
                isLoading = false,
                currentMonth = currentMonth
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

    private fun getUserRoles(user: User): List<String> =
        user.role.split(",").map { it.trim() }

    private fun getCurrentMonthName(): String {
        val sdf = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale("pt", "BR"))
        return sdf.format(System.currentTimeMillis()).replaceFirstChar { it.uppercase() }
    }

    private fun getCurrentMonthKey(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }

    private suspend fun schedulePaymentNotificationForUser(user: User, defaultDay: Int) {
        val roles = getUserRoles(user)
        val isFinanceiro = RolesEnum.FINANCEIRO.name in roles

        val dayToNotify = if (isFinanceiro) {
            getRepublicByIdUseCase(user.republicId)?.billsDueDay ?: 1
        } else {
            defaultDay
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_MONTH, dayToNotify)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            add(Calendar.DAY_OF_MONTH, -1)
        }

        val triggerDate = calendar.time
        val notificationId = (user.republicId + dayToNotify).hashCode()
        currentNotificationId = notificationId

        rentNotificationManager.scheduleRentNotification(
            id = notificationId,
            triggerAt = triggerDate,
            republicName = user.republicName,
            amount = 0.0,
            isBillsNotification = isFinanceiro
        )
    }

    private fun scheduleNotificationForNonFinanceUser(user: User) {
        viewModelScope.launch {
            schedulePaymentNotificationForUser(user, _uiState.value.day)
        }
    }
}

data class RentPaymentConfigUiState(
    val isAuthorized: Boolean = false,
    val day: Int = 1,
    val isFixed: Boolean = true,
    val user: User? = null,
    val isLoading: Boolean = true,
    val residentsWithStatus: List<ResidentPaymentStatus> = emptyList(),
    val currentMonth: String = ""
)

data class ResidentPaymentStatus(
    val resident: User,
    val hasPaid: Boolean
)
