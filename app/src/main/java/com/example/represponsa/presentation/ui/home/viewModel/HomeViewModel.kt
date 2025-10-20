package com.example.represponsa.presentation.ui.home.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.annotation.meta.TypeQualifierNickname
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _nickName = mutableStateOf("")
    val nickName: State<String> = _nickName

    private val _republicName = mutableStateOf("")
    val republicName: State<String> = _republicName

    private val _residentsPoints = mutableStateOf<List<UserPoints>>(emptyList())
    val residentsPoints: State<List<UserPoints>> = _residentsPoints

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            loadUserData()
        }
    }

    private suspend fun loadUserData() {
        _isLoading.value = true
        try {
            val currentUser = authRepository.getCurrentUser()
            _userName.value = currentUser?.userName ?: ""
            _nickName.value = currentUser?.nickName ?: ""
            _republicName.value = currentUser?.republicName ?: ""

            currentUser?.republicId?.let { republicId ->
                val residents = userRepository.getResidentsByRepublic(republicId)
                _residentsPoints.value = residents.map { user ->
                    UserPoints(
                        userName = user.userName,
                        points = user.monthlyPoints,
                        nickname = user.nickName
                    )
                }
            }
        } catch (e: Exception) {
            _residentsPoints.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

data class UserPoints(
    val userName: String,
    val nickname: String,
    val points: Int
)
