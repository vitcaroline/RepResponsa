package com.example.represponsa.presentation.ui.registerUser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Republic
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.RepublicRepository
import com.example.represponsa.presentation.ui.commons.validateName
import com.example.represponsa.presentation.ui.commons.validatePassword
import com.example.represponsa.presentation.ui.commons.validatePasswordConfirmation
import com.example.represponsa.presentation.ui.commons.validatePhone
import com.example.represponsa.presentation.ui.registerUser.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val republicRepository: RepublicRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _allRepublics = MutableStateFlow<List<Republic>>(emptyList())
    val allRepublics: StateFlow<List<Republic>> = _allRepublics

    init {
        loadRepublics()
    }

    private fun loadRepublics() {
        republicRepository.getAllRepublics { result ->
            _allRepublics.value = result.getOrDefault(emptyList())
        }
    }

    fun onFirstNameChange(value: String) {
        _state.update { it.copy(
            userName = value,
            userNameError = value.validateName()
        ) }
    }

    fun onNicknameChange(value: String) {
        _state.update { it.copy(
            nickname = value,
            nicknameError = value.validateName()
        ) }
    }

    fun onPhoneChange(value: String) {
        _state.update { it.copy(
            phone = value,
            phoneError = value.validatePhone()
        ) }
    }

    fun onPasswordChange(value: String) {
        _state.update {
            it.copy(
                password = value,
                passwordError = value.validatePassword(),
                confirmPwdError = if (it.hasTypedConfirmPwd && it.confirmPwd != value)
                    "As senhas não conferem" else null
            )
        }
    }

    fun onConfirmPwdChange(value: String) {
        _state.update {
            it.copy(
                confirmPwd = value,
                hasTypedConfirmPwd = true,
                confirmPwdError = if (value != it.password) "As senhas não conferem" else null
            )
        }
    }
    fun onRepublicChange(s: String)  = updateState { copy(republic = s) }
    fun onEmailChange(s: String)   = updateState { copy(email = s) }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        val st = state.value

        val userNameErr   = st.userName.validateName()
        val nickNameErr    = st.nickname.validateName()
        val phoneErr       = st.phone.validatePhone()
        val passwordErr    = st.password.validatePassword()
        val confirmPwdErr = validatePasswordConfirmation(st.password, st.confirmPwd)

        if (listOf(userNameErr, nickNameErr, phoneErr, passwordErr, confirmPwdErr).any { it != null }) {
            updateState {
                copy(
                    userNameError   = userNameError,
                    nicknameError    = nicknameError,
                    phoneError       = phoneErr,
                    passwordError    = passwordErr,
                    confirmPwdError  = confirmPwdErr
                )
            }
            return@launch
        }

        updateState {
            copy(
                userNameError   = null,
                nicknameError    = null,
                phoneError       = null,
                passwordError    = null,
                confirmPwdError  = null,
                isLoading        = true
            )
        }

        val selectedRepublic = _allRepublics.value.find { it.name == st.republic }

        if (selectedRepublic == null) {
            onError("República não encontrada.")
            updateState { copy(isLoading = false) }
            return@launch
        }

        val newUser = User(
            userName    = st.userName,
            nickName     = st.nickname,
            email        = st.email,
            phone        = st.phone,
            republicName = selectedRepublic.name,
            republicId   = selectedRepublic.id,
            role         = st.selectedRoles.joinToString(",") { it.name }
        )

        val result = repository.register(newUser, st.password)

        updateState { copy(isLoading = false) }
        if (result.isSuccess) {
            onSuccess()
        } else {
            onError(result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido")
        }
    }

    private inline fun updateState(update: RegisterState.() -> RegisterState) {
        _state.value = _state.value.update()
    }

    fun onRoleToggle(role: RolesEnum) {
        val currentRoles = _state.value.selectedRoles.toMutableList()
        if (currentRoles.contains(role)) {
            currentRoles.remove(role)
        } else {
            currentRoles.add(role)
        }
        updateState { copy(selectedRoles = currentRoles) }
    }

}