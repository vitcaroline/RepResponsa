package com.example.represponsa.presentation.ui.minutes.minutesList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.R
import com.example.represponsa.data.model.Minute
import com.example.represponsa.di.MinutesListViewModelFactory
import com.example.represponsa.presentation.ui.commons.EmptyState
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.minutes.minutesList.viewModel.MinutesListViewModel

@Composable
fun MinutesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreateMinute: () -> Unit,
    onNavigateToMinuteDetail: (Minute) -> Unit,
    viewModel: MinutesListViewModel = viewModel(factory = MinutesListViewModelFactory)
) {
    val minutes by viewModel.minutes

    val isLoading by viewModel.isLoading

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchMinutes()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Atas",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateMinute,
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Ata"
                )
            }
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            minutes.isEmpty() -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    EmptyState(
                        imageRes = R.drawable.ic_minutes_empty_state,
                        message = "Ainda não existem atas.",
                        buttonText = "Voltar",
                        onButtonClick = onNavigateBack
                    )
                }
            }
            else -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    MinuteList(
                        minutes = minutes,
                        onMinuteClick = { selectedMinute ->
                            if (selectedMinute.id.isNotBlank()) {
                                onNavigateToMinuteDetail(selectedMinute)
                            }
                        }
                    )
                }
            }
        }
    }
}