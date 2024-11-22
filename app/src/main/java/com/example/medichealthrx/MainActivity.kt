package com.example.medichealthrx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.core.view.WindowCompat
import com.example.medichealthrx.ui.ui.AppNavHost
import com.example.medichealthrx.ui.ui.theme.MedicHealthRxTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permitir que el contenido ocupe toda la pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MedicHealthRxTheme {
                // Llamar AppNavHost con el FragmentManager
                AppNavHost(fragmentManager = supportFragmentManager)
            }
        }
    }
}