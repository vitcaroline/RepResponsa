package com.example.represponsa.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.font.FontWeight
import com.example.represponsa.presentation.ui.commons.TopBar

data class ConfigOption(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun SettingsScreen(
    onNavigateBack :() -> Unit,
    onEditRepublic: () -> Unit,
    onRemoveResident: () -> Unit
) {
    val options = listOf(
        ConfigOption(
            label = "Editar informações da república",
            icon = Icons.Default.Edit,
            onClick = onEditRepublic
        ),
        ConfigOption(
            label = "Remover morador",
            icon = Icons.Default.Delete,
            onClick = onRemoveResident
        )
    )

    Scaffold(
        topBar = {
            TopBar(
                title = "Configurações",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { option.onClick() }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.label,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (index < options.lastIndex) {
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp))
                }
            }
        }
    }
}
