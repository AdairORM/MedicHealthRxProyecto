package com.example.medichealthrx


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medichealthrx.ui.ui.AppNavHost
import com.example.medichealthrx.ui.ui.theme.MedicHealthRxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicHealthRxTheme {
                AppNavHost() // Configura la navegación
            }
        }
    }
}
