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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val alarms = viewModel.alarms.collectAsState().value
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedAlarms by remember { mutableStateOf(setOf<Int>()) }
    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var loadingUserData by remember { mutableStateOf(true) }

    // Obtener los datos del usuario desde Firebase Firestore
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userData = document.data
                    }
                    loadingUserData = false
                }
                .addOnFailureListener {
                    loadingUserData = false
                }
        } else {
            loadingUserData = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo de la aplicación",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = userData?.get("name") as? String ?: "MedicHealth(RX)",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            val alarmsToDelete = alarms.filter { selectedAlarms.contains(it.alarmId) }
                            alarmsToDelete.forEach { viewModel.delete(it) }

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
                FloatingActionButton(
                    onClick = { navController.navigate("add_alarm") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Alarma")
                }
            }
        }
    ) { padding ->
        if (loadingUserData) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (alarms.isEmpty()) {
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
                        onClick = {
                            if (isSelectionMode) {
                                selectedAlarms = if (selectedAlarms.contains(alarm.alarmId)) {
                                    selectedAlarms - alarm.alarmId
                                } else {
                                    selectedAlarms + alarm.alarmId
                                }

                                if (selectedAlarms.isEmpty()) {
                                    isSelectionMode = false
                                }
                            } else {
                                navController.navigate("edit_alarm/${alarm.alarmId}")
                            }
                        },
                        onLongPress = {
                            isSelectionMode = true
                            selectedAlarms = selectedAlarms + alarm.alarmId
                        }
                    )
                }
            }
        }
    }
}