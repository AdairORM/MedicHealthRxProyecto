package com.example.medichealthrx.ui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medichealthrx.data.model.Alarm
import com.example.medichealthrx.viewmodel.MainViewModel
import java.util.*

@Composable
fun AddEditAlarmScreen(
    navController: NavController,
    viewModel: MainViewModel, // Ahora se recibe como parámetro
    alarmToEdit: Alarm? = null
) {
    var name by remember { mutableStateOf(alarmToEdit?.name ?: "") }
    var hour by remember { mutableStateOf(alarmToEdit?.hour ?: 12) }
    var minute by remember { mutableStateOf(alarmToEdit?.minute ?: 0) }
    var isPM by remember { mutableStateOf(alarmToEdit?.state == "PM") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo para el nombre de la alarma
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de la alarma") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de hora
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { if (hour > 1) hour-- }) { Text("-") }
            Text("$hour : $minute", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { if (hour < 12) hour++ }) { Text("+") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selector AM/PM
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("AM")
            Switch(
                checked = isPM,
                onCheckedChange = { isPM = it }
            )
            Text("PM")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de guardar
        Button(
            onClick = {
                val alarm = Alarm(
                    alarmId = alarmToEdit?.alarmId ?: 0,
                    name = name,
                    hour = hour,
                    minute = minute,
                    state = if (isPM) "PM" else "AM",
                    checked = false,
                    timeInMillis = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, if (isPM) hour + 12 else hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                )

                if (alarmToEdit == null) {
                    viewModel.insert(alarm) // Crear nueva alarma
                } else {
                    viewModel.update(alarm) // Editar alarma existente
                }

                navController.navigateUp() // Regresar a la pantalla principal
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
