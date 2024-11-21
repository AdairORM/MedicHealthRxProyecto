package com.example.medichealthrx.ui.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medichealthrx.R
import com.example.medichealthrx.ui.ui.components.AlarmItem
import com.example.medichealthrx.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val alarms = viewModel.alarms.collectAsState().value
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedAlarms by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Fila con logo y nombre centrados
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Logo
                        Image(
                            painter = painterResource(id = R.drawable.logo), // Imagen del logo
                            contentDescription = "Logo de la aplicación",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(end = 8.dp) // Espaciado entre el logo y el texto
                        )
                        // Texto del título
                        Text(
                            text = "MedicHealth(RX)",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            // Eliminar alarmas seleccionadas
                            val alarmsToDelete = alarms.filter { selectedAlarms.contains(it.alarmId) }
                            alarmsToDelete.forEach { viewModel.delete(it) }

                            // Limpiar el estado de selección y salir del modo de selección
                            selectedAlarms = emptySet()
                            isSelectionMode = false
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar seleccionadas")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(onClick = { navController.navigate("add_alarm") }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Alarma")
                }
            }
        }
    ) { padding ->
        if (alarms.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay alarmas programadas")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(alarms.size) { index ->
                    val alarm = alarms[index]
                    AlarmItem(
                        alarm = alarm,
                        isSelected = selectedAlarms.contains(alarm.alarmId),
                        onClick = { clickedAlarm ->
                            if (isSelectionMode) {
                                // Actualizar selección
                                selectedAlarms = if (selectedAlarms.contains(clickedAlarm.alarmId)) {
                                    selectedAlarms - clickedAlarm.alarmId
                                } else {
                                    selectedAlarms + clickedAlarm.alarmId
                                }

                                // Salir del modo de selección si no queda ninguna seleccionada
                                if (selectedAlarms.isEmpty()) {
                                    isSelectionMode = false
                                }
                            }
                        },
                        onLongPress = { longPressedAlarm ->
                            // Activar modo de selección múltiple
                            isSelectionMode = true
                            selectedAlarms = selectedAlarms + longPressedAlarm.alarmId
                        }
                    )
                }
            }
        }
    }
}
