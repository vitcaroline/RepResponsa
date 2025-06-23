package com.example.represponsa.presentation.ui.residents.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import kotlinx.coroutines.launch

class ResidentListViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchResidents()
    }

    private fun fetchResidents() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                if (user != null) {
                    val list = userRepository.getResidentsByRepublic(user.republicId)
                    _residents.value = list
                }
            } catch (e: Exception) {
                _residents.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}