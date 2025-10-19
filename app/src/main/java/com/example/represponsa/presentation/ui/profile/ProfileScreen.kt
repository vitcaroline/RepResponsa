package com.example.represponsa.presentation.ui.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.data.model.RolesEnum
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.profile.viewModel.ProfileViewModel
import com.example.represponsa.presentation.ui.profile.viewModel.UpdateState

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val user by viewModel.user
    val isLoading by viewModel.isLoading
    val isEditing by viewModel.isEditing
    val errors by viewModel.errors
    val updateState by viewModel.updateState.collectAsState()

    var editedUser by remember(user) { mutableStateOf(user) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Meu Perfil",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            if (user != null && !isLoading) {
                FloatingActionButton(
                    onClick = { viewModel.toggleEditing() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Cancelar edição" else "Editar perfil"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)) {

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                user == null -> {
                    Text("Erro ao carregar dados do usuário.", modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Informações Pessoais",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(16.dp))

                            if (isEditing) {
                                EditableTextField(
                                    label = "Nome",
                                    value = editedUser?.userName ?: "",
                                    error = errors.nameError
                                ) { editedUser = editedUser?.copy(userName = it) }

                                EditableTextField(
                                    label = "Apelido",
                                    value = editedUser?.nickName ?: "",
                                    error = errors.nicknameError
                                ) { editedUser = editedUser?.copy(nickName = it) }

                                EditableTextField(
                                    label = "Email",
                                    value = editedUser?.email ?: "",
                                    error = errors.emailError
                                ) { editedUser = editedUser?.copy(email = it) }

                                EditableTextField(
                                    label = "Telefone",
                                    value = editedUser?.phone ?: "",
                                    error = errors.phoneError,
                                    keyboardType = KeyboardType.Phone
                                ) { editedUser = editedUser?.copy(phone = it) }

                                EditableMultiRoleChips(
                                    label = "Funções",
                                    currentRole = editedUser?.role ?: "",
                                    onRolesChanged = { editedUser = editedUser?.copy(role = it) }
                                )

                                Button(
                                    onClick = { editedUser?.let { viewModel.updateUser(it) } },
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 14.dp)
                                ) {
                                    Text("Salvar alterações")
                                }

                                when (updateState) {
                                    is UpdateState.Loading -> CircularProgressIndicator(
                                        modifier = Modifier.align(
                                            Alignment.CenterHorizontally
                                        )
                                    )

                                    is UpdateState.Error ->
                                        Toast.makeText(context, "Informações não puderam ser atualizadas", Toast.LENGTH_SHORT).show()

                                    else -> {}
                                }
                            } else {

                                Column(modifier = Modifier.padding(16.dp)){
                                    ProfileField(label = "Nome", value = user!!.userName)
                                    ProfileField(label = "Apelido", value = user!!.nickName)
                                    ProfileField(label = "Email", value = user!!.email)
                                    ProfileField(label = "Telefone", value = user!!.phone)
                                    ProfileField(label = "República", value = user!!.republicName)
                                    ProfileField(label = "Função", value = user!!.role.lowercase())

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier.padding(vertical = 4.dp))
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
    Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun EditableMultiRoleChips(
    label: String,
    currentRole: String,
    onRolesChanged: (String) -> Unit
) {
    val selectedRoles = remember(currentRole) {
        currentRole.split(", ").filter { it.isNotBlank() }
    }.toMutableStateList()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            RolesEnum.entries.forEach { role ->
                val isSelected = selectedRoles.contains(role.label)
                val backgroundColor = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant

                val textColor = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface

                androidx.compose.material3.Surface(
                    color = backgroundColor,
                    shape = RoundedCornerShape(50),
                    tonalElevation = if (isSelected) 4.dp else 0.dp,
                    modifier = Modifier.clickable {
                        if (isSelected) {
                            selectedRoles.remove(role.label)
                        } else {
                            selectedRoles.add(role.label)
                        }
                        onRolesChanged(selectedRoles.joinToString(", "))
                    }
                ) {
                    Text(
                        text = role.label,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}