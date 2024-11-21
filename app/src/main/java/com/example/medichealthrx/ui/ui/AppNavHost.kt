package com.example.medichealthrx.ui.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medichealthrx.ui.ui.screens.AddEditAlarmScreen
import com.example.medichealthrx.ui.ui.screens.LoginScreen
import com.example.medichealthrx.ui.ui.screens.MainScreen
import com.example.medichealthrx.viewmodel.MainViewModel
import com.example.medichealthrx.viewmodel.MainViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medichealthrx.data.database.AlarmDatabase

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    fragmentManager: FragmentManager // Recibe el FragmentManager de la actividad
) {
    // Configurar el ViewModel con Room
    val context = androidx.compose.ui.platform.LocalContext.current
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(alarmDao))

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("main_screen") {
            MainScreen(navController, viewModel)
        }
        composable("add_alarm") {
            AddEditAlarmScreen(navController, viewModel, context = context)
        }
    }
}
