package com.example.medichealthrx.ui.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medichealthrx.ui.ui.screens.AddEditAlarmScreen
import com.example.medichealthrx.ui.ui.screens.LoginScreen
import com.example.medichealthrx.ui.ui.screens.MainScreen
import com.example.medichealthrx.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(), // Crea un NavController si no se pasa uno
    fragmentManager: androidx.fragment.app.FragmentManager
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val alarmDao = com.example.medichealthrx.data.database.AlarmDatabase.getInstance(context).alarmDao
    val viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.medichealthrx.viewmodel.MainViewModelFactory(alarmDao)
    )

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable("main_screen") { MainScreen(navController, viewModel) }
        composable("add_alarm") { AddEditAlarmScreen(navController, viewModel, context) }
        composable("edit_alarm/{alarmId}") { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId")?.toIntOrNull()
            AddEditAlarmScreen(navController, viewModel, context, alarmId)
        }
    }
}

