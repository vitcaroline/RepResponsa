package com.example.represponsa.presentation.ui.commons

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    label: String = "Selecionar prazo"
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formattedDate = formatter.format(selectedDate)

    var showDialog by remember { mutableStateOf(false) }

    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.time
    )

    Button(onClick = { showDialog = true }) {
        Text("$label: $formattedDate")
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { utcMillis ->
                        val localDate = Instant.ofEpochMilli(utcMillis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()

                        val correctedDate = Date.from(
                            localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )

                        onDateSelected(correctedDate)
                    }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}