package com.example.represponsa.presentation.ui.registerRepublic

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.di.auth.RegisterRepublicViewModelFactory
import com.example.represponsa.presentation.ui.commons.NumberDropdownField
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.registerRepublic.viewModel.RegisterRepublicViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterRepublicScreen(
    onCreateSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterRepublicViewModel = viewModel(factory = RegisterRepublicViewModelFactory)
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val availableRoles = viewModel.getAvailableRoles()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Cadastrar República",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nome da República") },
                singleLine = true,
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    state.nameError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            OutlinedTextField(
                value = state.address,
                onValueChange = viewModel::onAddressChange,
                label = { Text("Endereço") },
                singleLine = true,
                isError = state.addressError != null,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    state.addressError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                NumberDropdownField(
                    label = "Pets",
                    value = state.petCount,
                    onValueChange = viewModel::onPetCountChange,
                    options = (0..10).toList(),
                    modifier = Modifier.weight(1f)
                )
                state.petCountError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }

                Column(modifier = Modifier.weight(1f)) {
                    NumberDropdownField(
                        label = "Moradores",
                        value = state.residentCount,
                        onValueChange = viewModel::onResidentCountChange,
                        options = (1..30).toList(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.residentCountError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Funções", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                availableRoles.forEach { role ->
                    FilterChip(
                        selected = state.selectedRoles.contains(role),
                        onClick = { viewModel.onRoleToggle(role) },
                        label = { Text(role.label) },
                        modifier = Modifier.padding(end = 6.dp, bottom = 2.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color.Black
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.createRepublic(
                        onSuccess = {
                            Toast
                                .makeText(
                                    context,
                                    "Cadastro realizado com sucesso!",
                                    Toast.LENGTH_SHORT)
                                .show()
                            onCreateSuccess()
                        },
                        onError = { errorMessage = it }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar República")
            }

            errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}