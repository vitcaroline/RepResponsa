package com.example.represponsa.presentation.ui.receipts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.receipts.viewModel.RentPaymentConfigViewModel
import com.example.represponsa.presentation.ui.residents.viewModel.ResidentListViewModel

@Composable
fun ResidentsPaymentScreen(
    onNavigateBack: () -> Unit,
    residentListViewModel: ResidentListViewModel = hiltViewModel(),
    rentPaymentConfigViewModel: RentPaymentConfigViewModel = hiltViewModel()
) {
    val isLoading by residentListViewModel.isLoading
    var showDialog by remember { mutableStateOf(false) }

    val residents by residentListViewModel.residents

    val configUiState by rentPaymentConfigViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Pagamento de Aluguel",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "Moradores Pagantes",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(residents) { index, resident ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(resident.nickName)
                                    Checkbox(
                                        checked = false,
                                        onCheckedChange = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (!configUiState.isFixed) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Configurar Pagamento")
                }
            }

            if (showDialog) {
                RentPaymentConfigDialog(
                    viewModel = rentPaymentConfigViewModel,
                    onDismiss = { showDialog = false },
                    onConfigSaved = {
                    }
                )
            }
        }
    }
}
