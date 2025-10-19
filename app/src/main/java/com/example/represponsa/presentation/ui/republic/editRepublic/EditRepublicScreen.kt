package com.example.represponsa.presentation.ui.republic.editRepublic

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.republic.editRepublic.viewModel.EditRepublicViewModel
import com.example.represponsa.presentation.ui.republic.editRepublic.viewModel.RepublicUpdateState

@Composable
fun EditRepublicScreen(
    onNavigateBack: () -> Unit,
    viewModel: EditRepublicViewModel = hiltViewModel()
) {
    val republicState by viewModel.republicState
    val updateState by viewModel.updateState.collectAsState(initial = RepublicUpdateState.Idle)
    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current


    LaunchedEffect(updateState) {
        when (updateState) {
            is RepublicUpdateState.Success -> {
                Toast.makeText(context, "República atualizada com sucesso!", Toast.LENGTH_SHORT)
                    .show()
                isEditing = false
                viewModel.resetUpdateState()
            }

            is RepublicUpdateState.Error -> {
                val msg = (updateState as RepublicUpdateState.Error).message
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateState()
            }

            else -> {

            }
        }
    }
            Scaffold(
        topBar = {
            TopBar(
                title = "Editar República",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            if (!republicState.isLoading) {
                FloatingActionButton(
                    onClick = { isEditing = !isEditing },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Cancelar edição" else "Editar república"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                republicState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                "Informações da República",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(16.dp))

                            if (isEditing) {
                                EditableTextField(
                                    label = "Nome",
                                    value = republicState.name,
                                    error = republicState.nameError,
                                    onValueChange = viewModel::onNameChange
                                )
                                EditableTextField(
                                    label = "Endereço",
                                    value = republicState.address,
                                    error = republicState.addressError,
                                    onValueChange = viewModel::onAddressChange
                                )
                                EditableTextField(
                                    label = "Número de Moradores",
                                    value = republicState.numResidents,
                                    error = republicState.numResidentsError,
                                    keyboardType = KeyboardType.Number,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() }) {
                                            viewModel.onNumResidentsChange(newValue)
                                        }
                                    }
                                )
                                EditableTextField(
                                    label = "Número de Pets",
                                    value = republicState.numPets,
                                    error = republicState.numPetsError,
                                    keyboardType = KeyboardType.Number,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() }) {
                                            viewModel.onNumPetsChange(newValue)
                                        }
                                    }
                                )

                                Button(
                                    onClick = { viewModel.saveRepublic() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text("Salvar alterações")
                                }

                            }

                            else {
                                RepublicInfoItem(label = "Nome", value = republicState.name)
                                RepublicInfoItem(label = "Endereço", value = republicState.address)
                                RepublicInfoItem(label = "Número de Moradores", value = republicState.numResidents)
                                RepublicInfoItem(label = "Número de Pets", value = republicState.numPets)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepublicInfoItem(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value.ifEmpty { "—" },
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun EditableTextField(
    label: String,
    value: String,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}