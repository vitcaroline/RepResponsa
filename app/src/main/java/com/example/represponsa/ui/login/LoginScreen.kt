package com.example.represponsa.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.represponsa.R
import com.example.represponsa.di.LoginViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToCreateRepublic: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory)
) {
    val state by viewModel.state.collectAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.represponsa),
            contentDescription = "Logo do App",
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(
                    onSuccess = onLoginSuccess,
                    onError = { errorMessage = it }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Não tem conta? Cadastre-se")
        }

        TextButton(onClick = onNavigateToCreateRepublic) {
            Text("Cadastrar sua república")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Email ou senha incorretos", color = MaterialTheme.colorScheme.error)
        }
    }
}
