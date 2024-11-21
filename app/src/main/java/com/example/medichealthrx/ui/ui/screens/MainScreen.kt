package com.example.medichealthrx.ui.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen del logo con colores reales
                        Image(
                            painter = painterResource(id = R.drawable.logo), // Asegúrate de que el recurso 'logo' exista
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(32.dp) // Tamaño de la imagen
                                .padding(end = 8.dp)
                        )
                        // Título
                        Text("MedicHealth(Rx)")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("login_screen") }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_alarm") }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Alarma")
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
                    AlarmItem(alarm = alarms[index], onToggle = { alarm ->
                        viewModel.toggleAlarm(alarm)
                    })
                }
            }
        }
    }
}