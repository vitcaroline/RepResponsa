package com.example.represponsa.presentation.ui.minutes.editMinute

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.minutes.EditMinuteViewModelFactory
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.minutes.commons.MinuteForm
import com.example.represponsa.presentation.ui.minutes.commons.MinuteFormParams
import com.example.represponsa.presentation.ui.minutes.editMinute.viewModel.EditMinuteViewModel

@Composable
fun EditMinuteScreen(
    minuteId: String,
    onNavigateBack: () -> Unit,
    onNavigateToList: () -> Unit,
    viewModel: EditMinuteViewModel = viewModel(
        factory = EditMinuteViewModelFactory(minuteId)
    )
) {
    val state by viewModel.state
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(title = "Editar Ata", onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        MinuteForm(
            modifier = Modifier.padding(innerPadding),
            params = MinuteFormParams(
                title = state.title,
                onTitleChange = viewModel::onTitleChange,
                titleError = state.titleError,

                body = state.body,
                onBodyChange = viewModel::onBodyChange,
                bodyError = state.bodyError,

                onSubmit = {
                    viewModel.updateMinute(
                        onSuccess = {
                            Toast.makeText(context, "Ata atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            onNavigateToList()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                submitButtonText = "Salvar Alterações"
            )
        )
    }
}