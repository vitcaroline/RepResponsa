package com.example.represponsa.presentation.ui.minutes.createMinute.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Minute
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.domain.useCases.CreateMinuteUseCase
import com.example.represponsa.presentation.ui.commons.validateMinuteBody
import com.example.represponsa.presentation.ui.commons.validateMinuteTitle
import com.example.represponsa.presentation.ui.minutes.createMinute.MinuteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMinuteViewModel @Inject constructor(
    private val createMinuteUseCase: CreateMinuteUseCase,
    private val authRepo: AuthRepository,
) : ViewModel() {

    private val _state = mutableStateOf(MinuteUiState())
    val state: State<MinuteUiState> = _state

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value, titleError = null)
    }

    fun onBodyChange(value: String) {
        _state.value = _state.value.copy(body = value, bodyError = null)
    }

    fun validateFields(): Boolean {
        val current = _state.value

        val titleError = current.title.validateMinuteTitle()
        val bodyError = current.body.validateMinuteBody()

        _state.value = current.copy(
            titleError = titleError,
            bodyError = bodyError,
        )

        return titleError == null && bodyError == null
    }

    suspend fun createMinute(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        if (!validateFields()) return

        val currentUser = authRepo.getCurrentUser()
        val stateValue = _state.value

        if (currentUser == null) {
            onError("Erro ao carregar dados.")
            return
        }

        val minute = Minute(
            title = stateValue.title,
            body = stateValue.body
        )

        viewModelScope.launch {
            try {
                createMinuteUseCase(minute)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao criar tarefa")
            }
        }
    }
}