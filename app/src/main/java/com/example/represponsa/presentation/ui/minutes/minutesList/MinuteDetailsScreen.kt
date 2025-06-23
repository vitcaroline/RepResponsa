package com.example.represponsa.presentation.ui.minutes.minutesList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.data.model.Minute
import com.example.represponsa.di.minutes.MinuteDetailsViewModelFactory
import com.example.represponsa.presentation.ui.commons.ConfirmDeleteDialog
import com.example.represponsa.presentation.ui.commons.ExpandableFab
import com.example.represponsa.presentation.ui.commons.FabAction
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.minutes.minutesList.viewModel.MinuteDetailsViewModel

@Composable
fun MinuteDetailsScreen(
    minuteId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Minute) -> Unit,
    viewModel: MinuteDetailsViewModel = viewModel(factory = MinuteDetailsViewModelFactory(minuteId))
) {
    val minute by viewModel.minute
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (minute == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Detalhes da Ata",
                    onBackClick = onNavigateBack
                )
            },
            floatingActionButton = {
                ExpandableFab(
                    actions = listOf(
                        FabAction(
                            icon = Icons.Default.Edit,
                            label = "Editar",
                            onClick = { onNavigateToEdit(minute!!) }
                        ),
                        FabAction(
                            icon = Icons.Default.Delete,
                            label = "Excluir",
                            onClick = { showDialog = true }
                        )
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = minute?.title ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            modifier = Modifier
                                .heightIn(min = 150.dp)
                                .padding(top = 12.dp),
                            text = minute?.body ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }

        if (showDialog) {
            ConfirmDeleteDialog(
                message = "Deseja realmente excluir esta ata? Esta ação não pode ser desfeita.",
                onConfirm = {
                    showDialog = false
                    viewModel.deleteMinute(
                        onSuccess = {
                            Toast.makeText(context, "Ata excluída com sucesso", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}