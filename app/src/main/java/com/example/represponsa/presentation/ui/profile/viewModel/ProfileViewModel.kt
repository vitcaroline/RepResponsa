package com.example.represponsa.presentation.ui.profile.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
}