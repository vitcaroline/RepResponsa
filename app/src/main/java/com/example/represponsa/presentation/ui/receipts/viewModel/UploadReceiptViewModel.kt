package com.example.represponsa.presentation.ui.receipts.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.ReceiptRepository
import com.example.represponsa.domain.useCases.UploadReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadReceiptViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val receiptRepository: ReceiptRepository,
    private val uploadReceiptUseCase: UploadReceiptUseCase,
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    private val _alreadyUploaded = MutableStateFlow<Boolean?>(null)
    val alreadyUploaded: StateFlow<Boolean?> = _alreadyUploaded

    init {
        checkAlreadyUploaded()
    }

    fun checkAlreadyUploaded() {
        viewModelScope.launch {
            _alreadyUploaded.value = null // loading
            val user = authRepository.getCurrentUser()
            if (user == null) {
                _alreadyUploaded.value = false
                return@launch
            }

            val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
            val currentMonth = sdf.format(System.currentTimeMillis())

            val hasPaid = receiptRepository.hasUserPaidForMonth(user.uid, user.republicId, currentMonth)
            _alreadyUploaded.value = hasPaid
        }
    }

    fun uploadReceipt(fileUri: Uri, context: Context) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val result = uploadReceiptUseCase(fileUri, context)
            _uploadState.value = if (result.isSuccess) {
                UploadState.Success
            } else {
                UploadState.Error(result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido")
            }
        }
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}