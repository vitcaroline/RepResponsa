package com.example.represponsa.presentation.ui.receipts

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Switch
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.example.represponsa.presentation.ui.commons.DayOfMonthDropdown
import com.example.represponsa.presentation.ui.receipts.viewModel.ResidentsPaymentListViewModel

@Composable
fun RentPaymentConfigDialog(
    viewModel: ResidentsPaymentListViewModel = hiltViewModel(),
    onConfigSaved: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Configurar Pagamento de Aluguel", style = MaterialTheme.typography.titleMedium)

                DayOfMonthDropdown(
                    label = "Dia do Pagamento do Aluguel",
                    selectedDay = uiState.day,
                    onDaySelected = { viewModel.onDayChange(it) }
                )

                DayOfMonthDropdown(
                    label = "Dia do Pagamento das Contas",
                    selectedDay = uiState.billsDay,
                    onDaySelected = { viewModel.onBillsDayChange(it) }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = uiState.isFixed,
                        onCheckedChange = { viewModel.onFixedChange(it) }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (uiState.isFixed) "Mesma data todo os meses" else "Data pode mudar a cada mês")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            viewModel.saveConfig(
                                onSuccess = {
                                    onConfigSaved()
                                    Toast.makeText(context, "Configuração salva com sucesso!", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                },
                                onError = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    Log.e("RentPaymentConfig", "Erro ao salvar configuração: $message")
                                }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}