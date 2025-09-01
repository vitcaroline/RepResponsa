package com.example.represponsa.presentation.ui.residents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.data.model.User
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.residents.viewModel.ResidentListViewModel

@Composable
fun ResidentsListScreen(
    onNavigateBack: () -> Unit,
    viewModel: ResidentListViewModel = hiltViewModel()
) {
    val residents by viewModel.residents
    val isLoading by viewModel.isLoading

    Scaffold(
        topBar = {
            TopBar(title = "Moradores", onBackClick = {onNavigateBack()})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Moradores da República",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (residents.isEmpty()) {
                        Text("Nenhum morador encontrado.")
                    } else {
                        LazyColumn {
                            items(residents) { user ->
                                ResidentItem(user = user)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResidentItem(user: User) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(text = user.userName + " (${user.nickName})", style = MaterialTheme.typography.bodyLarge)
        Text(text = user.role.lowercase(), style = MaterialTheme.typography.bodyMedium)
        Text(text = user.email, style = MaterialTheme.typography.bodySmall, color = Color.Black)
    }
    Divider()
}