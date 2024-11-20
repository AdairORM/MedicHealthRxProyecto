package com.example.medichealthrx.ui.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medichealthrx.data.database.AlarmDatabase
import com.example.medichealthrx.data.model.Alarm
import com.example.medichealthrx.ui.ui.screens.AddEditAlarmScreen
import com.example.medichealthrx.ui.ui.screens.LoginScreen
import com.example.medichealthrx.ui.ui.screens.MainScreen
import com.example.medichealthrx.viewmodel.MainViewModel
import com.example.medichealthrx.viewmodel.MainViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val context = navController.context
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(alarmDao))

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable("main_screen") { MainScreen(navController, viewModel) }
        composable("add_alarm") { AddEditAlarmScreen(navController, viewModel) }
        composable("edit_alarm/{alarmId}") { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId")?.toIntOrNull()
            val alarm = navController.previousBackStackEntry?.arguments?.getParcelable<Alarm>("alarm")
            AddEditAlarmScreen(navController, viewModel, alarmToEdit = alarm)
        }
    }
}
