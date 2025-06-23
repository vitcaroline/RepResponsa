package com.example.represponsa.presentation.ui.minutes.createMinute

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.CreateMinuteViewModelFactory
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.minutes.commons.MinuteForm
import com.example.represponsa.presentation.ui.minutes.commons.MinuteFormParams
import com.example.represponsa.presentation.ui.minutes.createMinute.viewModel.CreateMinuteViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateMinuteScreen(
    onMinuteCreated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CreateMinuteViewModel = viewModel(factory = CreateMinuteViewModelFactory)
) {
    val state by viewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = "Criar nova ata",
                onBackClick = onNavigateBack
            )
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
                    if (!viewModel.validateFields()) return@MinuteFormParams

                    scope.launch {
                        viewModel.createMinute(
                            onSuccess = {
                                Toast.makeText(context, "Ata criada com sucesso!", Toast.LENGTH_SHORT).show()
                                onMinuteCreated()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                submitButtonText = "Salvar"
            )
        )
    }
}