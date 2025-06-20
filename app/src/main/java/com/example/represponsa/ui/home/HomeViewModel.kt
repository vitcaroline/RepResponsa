package com.example.represponsa.ui.home

import androidx.lifecycle.ViewModel
import com.example.represponsa.repository.AuthRepository

class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        authRepository.logout()
    }
}