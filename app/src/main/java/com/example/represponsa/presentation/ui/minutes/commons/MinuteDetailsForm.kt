package com.example.represponsa.presentation.ui.minutes.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MinuteFormParams(
    val title: String,
    val onTitleChange: (String) -> Unit,
    val titleError: String? = null,

    val body: String,
    val onBodyChange: (String) -> Unit,
    val bodyError: String? = null,

    val onSubmit: () -> Unit,
    val submitButtonText: String
)

@Composable
fun MinuteForm(
    modifier: Modifier = Modifier,
    params: MinuteFormParams
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = params.title,
                    onValueChange = params.onTitleChange,
                    label = { Text("Título da ata") },
                    isError = params.titleError != null,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (params.titleError != null) {
                    Text(
                        text = params.titleError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = params.body,
                    onValueChange = params.onBodyChange,
                    label = { Text("Descrição da ata") },
                    isError = params.bodyError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    maxLines = Int.MAX_VALUE,
                    singleLine = false
                )
                if (params.bodyError != null) {
                    Text(
                        text = params.bodyError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Button(
            onClick = params.onSubmit,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(params.submitButtonText)
        }
    }
}