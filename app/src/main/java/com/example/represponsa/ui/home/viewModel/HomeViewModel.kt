package com.example.represponsa.ui.home.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.repository.AuthRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _republicName = mutableStateOf("")
    val republicName: State<String> = _republicName


    init {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _userName.value = user?.firstName ?: ""
            _republicName.value = user?.republicName ?: ""
        }
    }
    fun logout() {
        authRepository.logout()
    }
}