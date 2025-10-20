package com.example.represponsa.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import com.example.represponsa.R
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.represponsa.presentation.ui.commons.UserAvatar
import com.example.represponsa.presentation.ui.home.viewModel.UserPoints

@Composable
fun PointsDashboard(
    residentsPoints: List<UserPoints>
) {
    val sortedResidents = residentsPoints.sortedByDescending { it.points }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SectionHeader(text = "Ranking do Mês")
        }
        itemsIndexed(sortedResidents) { index, resident ->
            ResidentPointCard(
                resident = resident,
                isTopResident = index == 0
            )
        }
    }
}

@Composable
fun ResidentPointCard(resident: UserPoints, isTopResident: Boolean) {
    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    modifier = Modifier.size(50.dp),
                    userName = resident.userName
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = resident.nickname,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${resident.points} pontos",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (isTopResident) {
                    Image(
                        painter = painterResource(id = R.drawable.medal),
                        contentDescription = "Primeiro colocado",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
