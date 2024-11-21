package com.example.medichealthrx.ui.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medichealthrx.data.model.Alarm
import com.example.medichealthrx.viewmodel.MainViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAlarmScreen(
    navController: NavController,
    viewModel: MainViewModel,
    fragmentManager: FragmentManager?, // FragmentManager requerido para el TimePicker
    alarmToEdit: Alarm? = null
) {
    if (fragmentManager == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: No se pudo abrir el TimePicker.")
        }
        return
    }

    var name by remember { mutableStateOf(alarmToEdit?.name ?: "") }
    var selectedHour by remember { mutableStateOf(alarmToEdit?.hour ?: 12) }
    var selectedMinute by remember { mutableStateOf(alarmToEdit?.minute ?: 0) }
    var isPM by remember { mutableStateOf(alarmToEdit?.state == "PM") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (alarmToEdit == null) "Agregar Alarma" else "Editar Alarma") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

            // Botón para seleccionar la hora
            Button(
                onClick = {
                    showTimePicker(
                        fragmentManager = fragmentManager,
                        initialHour = selectedHour,
                        initialMinute = selectedMinute,
                        onTimeSelected = { hour, minute ->
                            selectedHour = hour
                            selectedMinute = minute
                            isPM = hour >= 12
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Hora: ${formatTime(selectedHour, selectedMinute)}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar la alarma
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val alarm = Alarm(
                            alarmId = alarmToEdit?.alarmId ?: 0,
                            name = name,
                            hour = selectedHour,
                            minute = selectedMinute,
                            state = if (isPM) "PM" else "AM",
                            checked = false,
                            timeInMillis = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, selectedHour)
                                set(Calendar.MINUTE, selectedMinute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        )

                        if (alarmToEdit == null) {
                            viewModel.insert(alarm)
                        } else {
                            viewModel.update(alarm)
                        }

                        navController.navigateUp()
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Por favor, ingresa un nombre para la alarma",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Alarma")
            }
        }
    }
}

/**
 * Mostrar el MaterialTimePicker
 */
fun showTimePicker(
    fragmentManager: FragmentManager,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val picker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H) // Reloj circular en formato 12 horas
        .setHour(initialHour)
        .setMinute(initialMinute)
        .setTitleText("Seleccionar Hora")
        .build()

    picker.addOnPositiveButtonClickListener {
        onTimeSelected(picker.hour, picker.minute)
    }

    picker.show(fragmentManager, "timePicker")
}

/**
 * Formatear la hora en formato 12h legible
 */
fun formatTime(hour: Int, minute: Int): String {
    val formattedHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
    val formattedMinute = String.format("%02d", minute)
    val amPm = if (hour >= 12) "PM" else "AM"
    return "$formattedHour:$formattedMinute $amPm"
}
