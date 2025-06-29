package com.example.represponsa.presentation.ui.home.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}