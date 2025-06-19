package com.example.represponsa.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.login(state.value.email, state.value.password)
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido")
            }
        }
    }
}