package com.example.represponsa.presentation.ui.residents.removeResidents.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.User
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveResidentsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _residents = mutableStateOf<List<User>>(emptyList())
    val residents: State<List<User>> = _residents

    private val _selectedResidents = mutableStateOf<Set<String>>(emptySet())
    val selectedResidents: State<Set<String>> = _selectedResidents

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _isRemoving = mutableStateOf(false)
    val isRemoving: State<Boolean> = _isRemoving

    private val _showConfirmationDialog = mutableStateOf(false)
    val showConfirmationDialog: State<Boolean> = _showConfirmationDialog

    private val _toastMessage = mutableStateOf<String?>(null)
    val toastMessage: State<String?> = _toastMessage

    init {
        loadResidents()
    }

    private fun loadResidents() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                currentUser?.let { user ->
                    _residents.value = userRepository.getResidentsByRepublic(user.republicId)
                }
            } catch (e: Exception) {
                _residents.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleSelection(userId: String) {
        _selectedResidents.value = if (_selectedResidents.value.contains(userId)) {
            _selectedResidents.value - userId
        } else {
            _selectedResidents.value + userId
        }
    }

    fun dismissConfirmationDialog() {
        _showConfirmationDialog.value = false
    }

    fun removeSelectedResidents() {
        viewModelScope.launch {
            _isRemoving.value = true
            try {
                selectedResidents.value.forEach { id ->
                    userRepository.removeUser(id)
                }
                _toastMessage.value = "Morador(es) removido(s) com sucesso!"
                loadResidents()
                _selectedResidents.value = emptySet()
            } catch (e: Exception) {
                Log.e("RemoveResidents", "Erro ao remover moradores: ${e.message}")
                _toastMessage.value = "Erro ao remover moradores."
            } finally {
                _isRemoving.value = false
                _showConfirmationDialog.value = false
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}