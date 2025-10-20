package com.example.represponsa.presentation.ui.republic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Republic
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.data.repository.RepublicRepository
import com.example.represponsa.presentation.ui.commons.validateAddress
import com.example.represponsa.presentation.ui.commons.validatePetCount
import com.example.represponsa.presentation.ui.commons.validateRepublicName
import com.example.represponsa.presentation.ui.commons.validateResidentCount
import com.example.represponsa.presentation.ui.republic.RegisterRepublicState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.represponsa.data.model.RentPaymentConfig
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterRepublicViewModel @Inject constructor(
    private val repository: RepublicRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterRepublicState())
    val state: StateFlow<RegisterRepublicState> = _state

    fun onNameChange(s: String) = updateState { copy(name = s, nameError = null) }
    fun onAddressChange(s: String) = updateState { copy(address = s, addressError = null) }
    fun onPetCountChange(s: String) = updateState { copy(petCount = s) }
    fun onResidentCountChange(s: String) = updateState { copy(residentCount = s, residentCountError = null) }
    fun onBillsDueDayChange(day: Int) = updateState{ copy(billsDueDay = day) }
    fun onRentDueDayChange(day: Int) = updateState{ copy(rentDueDay = day) }
    fun onRentDueFixedChange(fixed: Boolean) = updateState { copy(rentDueFixed = fixed) }

    fun onRoleToggle(role: RolesEnum) = updateState {
        val updated = if (selectedRoles.contains(role)) {
            selectedRoles - role
        } else {
            selectedRoles + role
        }
        copy(selectedRoles = updated)
    }

    fun getAvailableRoles(): List<RolesEnum> = RolesEnum.entries

    fun createRepublic(onSuccess: () -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        if (!validateFields()) return@launch

        updateState { copy(isLoading = true) }

        val petCount = state.value.petCount.toIntOrNull() ?: 0
        val residentCount = state.value.residentCount.toIntOrNull() ?: 0

        val republic = Republic(
            name = state.value.name,
            address = state.value.address,
            petCount = petCount,
            residentCount = residentCount,
            roles = state.value.selectedRoles.map { it.name },
            billsDueDay = state.value.billsDueDay,
            rentPaymentConfig = RentPaymentConfig(
                day = state.value.rentDueDay,
                isFixed = state.value.rentDueFixed
            )
        )

        val result = repository.createRepublic(republic)

        updateState { copy(isLoading = false) }

        if (result.isSuccess) onSuccess()
        else onError(result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido")
    }

    private fun validateFields(): Boolean {
        val nameError = state.value.name.validateRepublicName()
        val addressError = state.value.address.validateAddress()
        val residentCountError = state.value.residentCount.validateResidentCount()
        val petCountError = state.value.petCount.validatePetCount()

        _state.value = state.value.copy(
            nameError = nameError,
            addressError = addressError,
            residentCountError = residentCountError,
            petCountError = petCountError
        )

        return listOf(nameError, addressError, residentCountError).all { it == null }
    }

    private inline fun updateState(update: RegisterRepublicState.() -> RegisterRepublicState) {
        _state.value = _state.value.update()
    }
}