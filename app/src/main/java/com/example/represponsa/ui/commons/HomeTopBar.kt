package com.example.represponsa.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    userName: String,
    onOptionSelected: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row {
//                Icon(
//                    painter = painterResource(R.drawable.represponsa),
//                    contentDescription = "logo-icon",
//                    modifier = Modifier.size(50.dp)
//                )
                Text("RepResponsa")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            androidx.compose.material3.IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            Box(modifier = Modifier
                .wrapContentSize(Alignment.TopEnd)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Abrir menu",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color(0xFF004D40)) },
                        text = { Text("Perfil") },
                        onClick = {
                            expanded = false
                            onOptionSelected("profile")
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Settings, contentDescription = "Perfil", tint = Color(0xFF004D40)) },
                        text = { Text("Configurações") },
                        onClick = {
                            expanded = false
                            onOptionSelected("config")
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = "Perfil", tint = Color(0xFF004D40)) },
                        text = { Text("Sair") },
                        onClick = {
                            expanded = false
                            onOptionSelected("logout")
                        }
                    )
                }
            }
        }
    )
}