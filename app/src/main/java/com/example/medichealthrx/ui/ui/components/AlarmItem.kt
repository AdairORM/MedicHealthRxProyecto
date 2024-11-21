package com.example.medichealthrx.ui.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.medichealthrx.data.model.Alarm

@Composable
fun AlarmItem(
    alarm: Alarm,
    isSelected: Boolean,
    onClick: (Alarm) -> Unit,
    onLongPress: (Alarm) -> Unit // Callback para manejar clics prolongados
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) { // Detectar clics y clics prolongados
                detectTapGestures(
                    onTap = { onClick(alarm) },
                    onLongPress = { onLongPress(alarm) }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = alarm.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${alarm.hour}:${String.format("%02d", alarm.minute)} ${alarm.state}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionada",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
