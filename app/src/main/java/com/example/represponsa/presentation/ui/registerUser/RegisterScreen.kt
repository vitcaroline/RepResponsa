package com.example.represponsa.presentation.ui.registerUser

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.registerUser.viewModel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack:    () -> Unit,
    onNavigateToRegisterRepublic: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val republicList by viewModel.allRepublics.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Cadastrar Morador",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = state.firstName,
                onValueChange = viewModel::onFirstNameChange,
                label = { Text("Nome") },
                isError = state.firstNameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.firstNameError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.lastName,
                onValueChange = viewModel::onLastNameChange,
                label = { Text("Sobrenome") },
                isError = state.lastNameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.lastNameError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.phone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text("Telefone") },
                isError = state.phoneError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            state.phoneError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Senha") },
                isError = state.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.passwordError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.confirmPwd,
                onValueChange = viewModel::onConfirmPwdChange,
                label = { Text("Confirmar senha") },
                isError = state.confirmPwdError != null,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.confirmPwdError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.republic,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecione a república") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    republicList.forEach { republic ->
                        DropdownMenuItem(
                            text = { Text(republic.name) },
                            onClick = {
                                viewModel.onRepublicChange(republic.name)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.Start).padding(top = 8.dp)) {
                RadioButton(
                    selected = state.role == "morador",
                    onClick = { viewModel.onRoleChange("morador") }
                )
                Text(
                    text = "Morador",
                    modifier = Modifier
                        .padding(start = 4.dp, end = 16.dp)
                        .align(Alignment.CenterVertically)
                )

                RadioButton(
                    selected = state.role == "administrador",
                    onClick = { viewModel.onRoleChange("administrador") }
                )
                Text(
                    text = "Administrador",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Spacer(Modifier.height(16.dp))

            /* -------- Botões -------- */
            Button(
                onClick = {
                    viewModel.register(
                        onSuccess = {
                            Toast
                                .makeText(
                                    context,
                                    "Cadastro realizado com sucesso!",
                                    Toast.LENGTH_SHORT)
                                .show()
                            onRegisterSuccess()
                        },
                        onError = { errorMessage = it }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Cadastrar")
            }

            TextButton(onClick = onNavigateToRegisterRepublic) {
                Text("Sua República não está aqui? Cadastre agora.")
            }

            errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}