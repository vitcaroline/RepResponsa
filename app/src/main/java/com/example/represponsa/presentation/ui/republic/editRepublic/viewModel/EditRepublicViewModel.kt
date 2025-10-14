package com.example.represponsa.presentation.ui.republic.editRepublic.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Republic
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.GetRepublicByIdUseCase
import com.example.represponsa.domain.useCases.UpdateRepublicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRepublicViewModel @Inject constructor(
    private val getRepublicByIdUseCase: GetRepublicByIdUseCase,
    private val updateRepublicUseCase: UpdateRepublicUseCase,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _republicState = mutableStateOf(RepublicEditState())
    val republicState: State<RepublicEditState> = _republicState

    private val _updateState = MutableStateFlow<RepublicUpdateState>(RepublicUpdateState.Idle)
    val updateState: StateFlow<RepublicUpdateState> = _updateState

    init {
        loadCurrentRepublic()
    }

    private fun loadCurrentRepublic() {
        viewModelScope.launch {
            _republicState.value = _republicState.value.copy(isLoading = true)
            try {
                val user = authRepo.getCurrentUser()
                    ?: throw Exception("Usuário não logado")
                val republicId = user.republicId ?: throw Exception("República não encontrada")
                val republic = getRepublicByIdUseCase(republicId)
                    ?: throw Exception("República não encontrada")

                _republicState.value = _republicState.value.copy(
                    name = republic.name,
                    address = republic.address,
                    numResidents = republic.residentCount.toString(),
                    numPets = republic.petCount.toString(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _republicState.value = _republicState.value.copy(isLoading = false)
            }
        }
    }

    fun onNameChange(newName: String) {
        _republicState.value = _republicState.value.copy(name = newName)
    }

    fun onAddressChange(newAddress: String) {
        _republicState.value = _republicState.value.copy(address = newAddress)
    }

    fun onNumResidentsChange(newValue: String) {
        _republicState.value = _republicState.value.copy(numResidents = newValue)
    }

    fun onNumPetsChange(newValue: String) {
        _republicState.value = _republicState.value.copy(numPets = newValue)
    }

    fun saveRepublic() {
        val state = _republicState.value

        if (state.name.isBlank()) {
            _republicState.value = state.copy(nameError = "Nome não pode ficar vazio")
            return
        }

        if (state.address.isBlank()) {
            _republicState.value = state.copy(addressError = "Endereço não pode ficar vazio")
            return
        }

        val numResidents = state.numResidents.toIntOrNull()
        if (numResidents == null || numResidents <= 0) {
            _republicState.value = state.copy(numResidentsError = "Número de moradores inválido")
            return
        }

        val numPets = state.numPets.toIntOrNull()
        if (numPets == null || numPets < 0) {
            _republicState.value = state.copy(numPetsError = "Número de pets inválido")
            return
        }

        _updateState.value = RepublicUpdateState.Loading

        viewModelScope.launch {
            try {
                val user = authRepo.getCurrentUser() ?: throw Exception("Usuário não logado")
                val republicId = user.republicId ?: throw Exception("República não encontrada")

                val republic = Republic(
                    id = republicId,
                    name = state.name,
                    address = state.address,
                    residentCount = numResidents,
                    petCount = numPets
                )

                updateRepublicUseCase(republic)
                _updateState.value = RepublicUpdateState.Success
            } catch (e: Exception) {
                _updateState.value = RepublicUpdateState.Error(e.localizedMessage ?: "Erro desconhecido")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = RepublicUpdateState.Idle
    }
}



data class RepublicEditState(
    val name: String = "",
    val address: String = "",
    val numResidents: String = "",
    val numPets: String = "",
    val nameError: String? = null,
    val addressError: String? = null,
    val numResidentsError: String? = null,
    val numPetsError: String? = null,
    val isLoading: Boolean = true
)

sealed class RepublicUpdateState {
    object Idle : RepublicUpdateState()
    object Loading : RepublicUpdateState()
    object Success : RepublicUpdateState()
    data class Error(val message: String) : RepublicUpdateState()
}