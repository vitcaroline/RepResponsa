package com.example.represponsa.presentation.ui.profile.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.presentation.ui.commons.validateEmail
import com.example.represponsa.presentation.ui.commons.validateName
import com.example.represponsa.presentation.ui.commons.validatePhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading


    private val _isEditing = mutableStateOf(false)
    val isEditing: State<Boolean> = _isEditing

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    private val _errors = mutableStateOf(ProfileFormErrors())
    val errors: State<ProfileFormErrors> = _errors

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                _user.value = authRepository.getCurrentUser()
            } catch (e: Exception) {
                _user.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleEditing() {
        _isEditing.value = !_isEditing.value
    }

    fun updateUser(updatedUser: User) {
        if (!validateFields(updatedUser)) return

        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                authRepository.updateUser(updatedUser)
                _user.value = updatedUser
                _isEditing.value = false
                _updateState.value = UpdateState.Success("Informações atualizadas com sucesso!")
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error("Erro ao atualizar: ${e.localizedMessage}")
            }
        }
    }

    fun validateFields(user: User): Boolean {
        val nameError = user.userName.validateName()
        val nicknameError = if (user.nickName.isBlank()) "Apelido não pode ficar vazio" else null
        val emailError = validateEmail(user.email)
        val phoneError = user.phone.validatePhone()

        _errors.value = ProfileFormErrors(
            nameError = nameError,
            nicknameError = nicknameError,
            emailError = emailError,
            phoneError = phoneError
        )

        return listOf(nameError, nicknameError, emailError, phoneError).all { it == null }
    }
}

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    data class Success(val message: String) : UpdateState()
    data class Error(val message: String) : UpdateState()
}

data class ProfileFormErrors(
    val nameError: String? = null,
    val nicknameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null
)