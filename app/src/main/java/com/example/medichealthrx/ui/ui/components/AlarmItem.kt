package com.example.medichealthrx.ui.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medichealthrx.data.model.Alarm

@Composable
fun AlarmItem(alarm: Alarm, onToggle: (Alarm) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = alarm.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${alarm.hour}:${alarm.minute} ${alarm.state}",
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = alarm.checked,
                onCheckedChange = { onToggle(alarm) }
            )
        }
    }
}
