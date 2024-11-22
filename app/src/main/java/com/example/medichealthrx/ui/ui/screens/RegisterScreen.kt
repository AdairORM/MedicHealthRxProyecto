package com.example.medichealthrx.ui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrar Usuario",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Register button
        Button(
            onClick = {
                registerUser(email, password) { success, message ->
                    if (success) {
                        navController.navigate("main_screen") {
                            popUpTo("register_screen") { inclusive = true } // Limpia el stack
                        }
                    } else {
                        errorMessage = message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
    }
}

fun registerUser(
    email: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    if (email.isEmpty() || password.isEmpty()) {
        onResult(false, "Por favor ingresa tu correo y contraseña.")
        return
    }

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val user = mapOf(
                        "email" to email,
                        "userId" to userId,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("users").document(userId)
                        .set(user) // Guarda información del usuario en Firestore
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            onResult(false, "Error al guardar los datos del usuario: ${e.message}")
                        }
                } else {
                    onResult(false, "No se pudo obtener el ID del usuario.")
                }
            } else {
                onResult(false, task.exception?.message ?: "Error desconocido.")
            }
        }
}



fun saveUserToFirestore(userId: String, email: String, onResult: (Boolean, String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val user = hashMapOf(
        "userId" to userId,
        "email" to email,
        "createdAt" to System.currentTimeMillis()
    )

    db.collection("users")
        .document(userId)
        .set(user)
        .addOnSuccessListener {
            onResult(true, null) // Guardado exitoso
        }
        .addOnFailureListener { exception ->
            onResult(false, exception.message) // Error al guardar
        }
}
