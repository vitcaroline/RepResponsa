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
import com.google.accompanist.flowlayout.FlowRow
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.data.model.RolesEnum
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
                value = state.userName,
                onValueChange = viewModel::onFirstNameChange,
                label = { Text("Nome") },
                isError = state.userNameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.userNameError?.let { err ->
                Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.nickname,
                onValueChange = viewModel::onNicknameChange,
                label = { Text("Apelido") },
                isError = state.nicknameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            state.nicknameError?.let { err ->
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
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Selecione as funções do morador:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                RolesEnum.values().forEach { role ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .widthIn(min = 150.dp) // força largura mínima para colunas
                    ) {
                        Checkbox(
                            checked = state.selectedRoles.contains(role),
                            onCheckedChange = { viewModel.onRoleToggle(role) }
                        )
                        Text(
                            text = role.label,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
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