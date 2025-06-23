package com.example.represponsa.presentation.ui.minutes.minutesList.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.represponsa.data.model.Minute
import com.example.represponsa.domain.useCases.DeleteMinuteUseCase
import com.example.represponsa.domain.useCases.GetMinuteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MinuteDetailsViewModel @Inject constructor(
    private val getMinuteByIdUseCase: GetMinuteByIdUseCase,
    private val deleteMinuteUseCase: DeleteMinuteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _minute = mutableStateOf<Minute?>(null)
    val minute: State<Minute?> = _minute

    init {
        val minuteId = savedStateHandle.get<String>("minuteId")
        if (minuteId != null) {
            fetchMinute(minuteId)
        }
    }

    private fun fetchMinute(id: String) {
        viewModelScope.launch {
            _minute.value = getMinuteByIdUseCase(id)
        }
    }

    fun deleteMinute(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val minuteToDelete = _minute.value ?: return
        viewModelScope.launch {
            try {
                deleteMinuteUseCase(minuteToDelete.id)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao excluir ata.")
            }
        }
    }
}