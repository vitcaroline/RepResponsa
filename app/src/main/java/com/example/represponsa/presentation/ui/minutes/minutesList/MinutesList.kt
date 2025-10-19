package com.example.represponsa.presentation.ui.minutes.minutesList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.represponsa.data.model.Minute
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MinuteList(
    minutes: List<Minute>,
    onMinuteClick: (Minute) -> Unit
) {
    val rows = minutes.chunked(2)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rows) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (minute in rowItems) {
                    MinuteItem(
                        minute = minute,
                        onClick = { onMinuteClick(minute) },
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                    )
                }

                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MinuteItem(
    minute: Minute,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(minute.date)

    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = minute.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = minute.body,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Data: $formattedDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MinuteListPreview() {
    val sampleMinutes = listOf(
        Minute(
            id = "1",
            title = "Reunião Geral",
            body = "Discussão sobre a divisão das tarefas e novos moradores.",
            date = Date()
        ),
        Minute(
            id = "2",
            title = "Compras da Semana",
            body = "Aprovado o orçamento para compras da feira.",
            date = Date()
        ),
        Minute(
            id = "3",
            title = "Problemas na Cozinha",
            body = "Sugestão de conserto da torneira e substituição de lâmpadas.",
            date = Date()
        )
    )

    MinuteList(minutes = sampleMinutes, onMinuteClick = {})
}