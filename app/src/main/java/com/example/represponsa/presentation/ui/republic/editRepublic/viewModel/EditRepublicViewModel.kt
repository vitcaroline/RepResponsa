package com.example.represponsa.presentation.ui.republic.editRepublic.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Republic
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.GetRepublicByIdUseCase
import com.example.represponsa.domain.useCases.UpdateRepublicUseCase
import com.example.represponsa.presentation.ui.commons.validateAddress
import com.example.represponsa.presentation.ui.commons.validatePetCount
import com.example.represponsa.presentation.ui.commons.validateRepublicName
import com.example.represponsa.presentation.ui.commons.validateResidentCount
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
        _republicState.value = _republicState.value.copy(
            name = newName,
            nameError = newName.validateRepublicName()
        )
    }

    fun onAddressChange(newAddress: String) {
        _republicState.value = _republicState.value.copy(
            address = newAddress,
            addressError = newAddress.validateAddress()
        )
    }

    fun onNumResidentsChange(newValue: String) {
        _republicState.value = _republicState.value.copy(
            numResidents = newValue,
            numResidentsError = newValue.validateResidentCount()
        )
    }

    fun onNumPetsChange(newValue: String) {
        _republicState.value = _republicState.value.copy(
            numPets = newValue,
            numPetsError = newValue.validatePetCount()
        )
    }


    fun saveRepublic() {
        val state = _republicState.value

        val nameError = state.name.validateRepublicName()
        val addressError = state.address.validateAddress()
        val numResidentsError = state.numResidents.validateResidentCount()
        val numPetsError = state.numPets.validatePetCount()

        _republicState.value = state.copy(
            nameError = nameError,
            addressError = addressError,
            numResidentsError = numResidentsError,
            numPetsError = numPetsError
        )

        if (listOf(nameError, addressError, numResidentsError, numPetsError).any { it != null }) {
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
                    residentCount = state.numResidents.toInt(),
                    petCount = state.numPets.toIntOrNull() ?: 0
                )

                updateRepublicUseCase(republic)
                _updateState.value = RepublicUpdateState.Success
            } catch (e: Exception) {
                _updateState.value =
                    RepublicUpdateState.Error(e.localizedMessage ?: "Erro desconhecido")
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