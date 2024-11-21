package com.example.medichealthrx.ui.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medichealthrx.data.database.AlarmDatabase
import com.example.medichealthrx.ui.ui.screens.AddEditAlarmScreen
import com.example.medichealthrx.ui.ui.screens.LoginScreen
import com.example.medichealthrx.ui.ui.screens.MainScreen
import com.example.medichealthrx.viewmodel.MainViewModel
import com.example.medichealthrx.viewmodel.MainViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current

    // Verificar si el contexto es una FragmentActivity para obtener el FragmentManager
    val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager

    if (fragmentManager == null) {
        // Log para depuración si el FragmentManager es nulo
        Log.e("AppNavHost", "El FragmentManager es nulo. Context: ${context::class.java.name}")
    } else {
        // Log para verificar que el FragmentManager es válido
        Log.i("AppNavHost", "FragmentManager inicializado correctamente.")
    }

    // Inicializar el DAO y el ViewModel
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(alarmDao))

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable("main_screen") { MainScreen(navController, viewModel) }
        composable("add_alarm") {
            if (fragmentManager != null) {
                AddEditAlarmScreen(navController, viewModel, fragmentManager)
            }
            else {
                // Mostrar mensaje en la UI si no se puede abrir el TimePicker
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: No se pudo abrir el TimePicker.")
                }
            }
        }
    }
}
