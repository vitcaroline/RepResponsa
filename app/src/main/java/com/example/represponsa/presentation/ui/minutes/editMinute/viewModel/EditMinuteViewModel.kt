package com.example.represponsa.presentation.ui.minutes.editMinute.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Minute
import com.example.represponsa.domain.useCases.GetMinuteByIdUseCase
import com.example.represponsa.domain.useCases.UpdateMinuteUseCase
import com.example.represponsa.presentation.ui.commons.validateMinuteBody
import com.example.represponsa.presentation.ui.commons.validateMinuteTitle
import com.example.represponsa.presentation.ui.minutes.createMinute.MinuteUiState
import kotlinx.coroutines.launch

class EditMinuteViewModel(
    private val getMinuteByIdUseCase: GetMinuteByIdUseCase,
    private val updateMinuteUseCase: UpdateMinuteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(MinuteUiState())
    val state: State<MinuteUiState> = _state

    private var currentMinuteId: String? = null

    init {
        val minuteId = savedStateHandle.get<String>("minuteId")
        if (minuteId != null) {
            viewModelScope.launch {
                val minute = getMinuteByIdUseCase(minuteId)
                minute?.let {
                    currentMinuteId = it.id
                    _state.value = _state.value.copy(
                        title = it.title,
                        body = it.body
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value, titleError = null)
    }

    fun onBodyChange(value: String) {
        _state.value = _state.value.copy(body = value, bodyError = null)
    }

    private fun validate(): Boolean {
        val titleError = _state.value.title.validateMinuteTitle()
        val bodyError = _state.value.body.validateMinuteBody()

        _state.value = _state.value.copy(titleError = titleError, bodyError = bodyError)

        return titleError == null && bodyError == null
    }

    fun updateMinute(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validate()) return

        val id = currentMinuteId ?: return onError("Ata inválida")
        val minute = Minute(
            id = id,
            title = _state.value.title,
            body = _state.value.body
        )

        viewModelScope.launch {
            try {
                updateMinuteUseCase(minute)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao atualizar ata.")
            }
        }
    }
}