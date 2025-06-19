package com.example.represponsa.ui.registerRepublic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.model.Republic
import com.example.represponsa.model.RolesEnum
import com.example.represponsa.repository.RepublicRepository
import com.example.represponsa.ui.commons.validateAddress
import com.example.represponsa.ui.commons.validatePetCount
import com.example.represponsa.ui.commons.validateRepublicName
import com.example.represponsa.ui.commons.validateResidentCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterRepublicViewModel(
    private val repository: RepublicRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterRepublicState())
    val state: StateFlow<RegisterRepublicState> = _state

    fun onNameChange(s: String) = updateState { copy(name = s, nameError = null) }
    fun onAddressChange(s: String) = updateState { copy(address = s, addressError = null) }
    fun onPetCountChange(s: String) = updateState { copy(petCount = s) }
    fun onResidentCountChange(s: String) = updateState { copy(residentCount = s, residentCountError = null) }

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
            roles = state.value.selectedRoles.map { it.name }
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