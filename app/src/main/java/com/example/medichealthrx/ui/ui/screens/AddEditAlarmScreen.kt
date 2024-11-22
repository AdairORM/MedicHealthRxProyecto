package com.example.medichealthrx.ui.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medichealthrx.R
import com.example.medichealthrx.data.model.Alarm
import com.example.medichealthrx.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAlarmScreen(
    navController: NavController,
    viewModel: MainViewModel,
    context: Context,
    alarmId: Int? = null
) {
    val existingAlarm = alarmId?.let { viewModel.getAlarmById(it) }
    var name by remember { mutableStateOf(existingAlarm?.name ?: "") }
    var selectedHour by remember { mutableStateOf(existingAlarm?.hour ?: 12) }
    var selectedMinute by remember { mutableStateOf(existingAlarm?.minute ?: 0) }
    var selectedMedicine by remember { mutableStateOf("") } // Medicamento seleccionado
    var medicines by remember { mutableStateOf(listOf<String>()) } // Lista de medicamentos
    var expanded by remember { mutableStateOf(false) } // Control del menú desplegable

    // Cargar medicamentos desde Firestore
    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            val snapshot = db.collection("datosmedicamentos").get().await()
            if (snapshot.isEmpty) {
                Log.e("Firestore", "La colección 'datosmedicamentos' está vacía.")
            } else {
                medicines = snapshot.documents.mapNotNull { it.getString("nombre") }
                Log.d("Firestore", "Medicamentos cargados: $medicines")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al cargar medicamentos: ${e.message}")
            Toast.makeText(context, "Error al cargar medicamentos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (alarmId == null) "Agregar Alarma" else "Editar Alarma") },
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.medicamentos),
                contentDescription = "Imagen de medicamentos",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la alarma") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown para seleccionar un medicamento
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedMedicine,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Seleccionar Medicamento") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    medicines.forEach { medicine ->
                        DropdownMenuItem(
                            text = { Text(medicine) },
                            onClick = {
                                selectedMedicine = medicine
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    showTimePicker(context, selectedHour, selectedMinute) { hour, minute ->
                        selectedHour = hour
                        selectedMinute = minute
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Hora: $selectedHour:${String.format("%02d", selectedMinute)}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val alarm = Alarm(
                            alarmId = existingAlarm?.alarmId ?: 0,
                            name = name,
                            hour = selectedHour,
                            minute = selectedMinute,
                            state = if (selectedHour >= 12) "PM" else "AM",
                            checked = existingAlarm?.checked ?: false,
                            timeInMillis = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, selectedHour)
                                set(Calendar.MINUTE, selectedMinute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        )

                        if (alarmId == null) {
                            viewModel.insert(alarm)
                        } else {
                            viewModel.update(alarm)
                        }

                        navController.navigateUp()
                    } else {
                        Toast.makeText(context, "Por favor, ingresa un nombre para la alarma", Toast.LENGTH_SHORT).show()
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
 * Muestra un TimePickerDialog para seleccionar la hora.
 */
fun showTimePicker(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeSelected(hourOfDay, minute)
        },
        initialHour,
        initialMinute,
        false // Formato de 12 horas
    )
    timePickerDialog.show()
}
