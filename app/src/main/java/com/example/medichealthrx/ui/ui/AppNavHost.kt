package com.example.medichealthrx.ui.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medichealthrx.ui.ui.screens.AddEditAlarmScreen
import com.example.medichealthrx.ui.ui.screens.LoginScreen
import com.example.medichealthrx.ui.ui.screens.MainScreen
import com.example.medichealthrx.ui.ui.screens.RegisterScreen
import com.example.medichealthrx.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
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

    // Determina la pantalla inicial en función de si el usuario está autenticado
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        // Verifica si el usuario tiene correo verificado (opcional)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && user.isEmailVerified) {
            "main_screen"
        } else {
            "login_screen"
        }
    } else {
        "login_screen"
    }


    // Define un único NavHost
    NavHost(navController = navController, startDestination = startDestination) {
        // Pantalla de inicio de sesión
        composable("login_screen") {
            LoginScreen(navController)
        }

        // Pantalla principal
        composable("main_screen") {
            MainScreen(navController, viewModel)
        }

        // Pantalla para añadir alarmas
        composable("add_alarm") {
            AddEditAlarmScreen(navController, viewModel, context)
        }

        // Pantalla para editar alarmas
        composable("edit_alarm/{alarmId}") { backStackEntry ->
            val alarmId = backStackEntry.arguments?.getString("alarmId")?.toIntOrNull()
            AddEditAlarmScreen(navController, viewModel, context, alarmId)
        }

        // Pantalla de registro de usuario
        composable("register_screen") {
            RegisterScreen(navController)
        }
    }
}
