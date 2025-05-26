package com.example.materialplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthScreen(
    onAuthSuccess: (userId: String) -> Unit,
    onAuthFailure: (error: String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    // режим: вход или регистрация
    var isSignUp by rememberSaveable { mutableStateOf(false) }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSignUp) "Register" else "Login",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                errorMessage = null
                isLoading = true

                val operation = if (isSignUp) {
                    auth.createUserWithEmailAndPassword(email.trim(), password)
                } else {
                    auth.signInWithEmailAndPassword(email.trim(), password)
                }

                operation.addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onAuthSuccess(task.result?.user?.uid.orEmpty())
                    } else {
                        errorMessage = task.exception?.localizedMessage
                            ?: "Unknown error"
                        onAuthFailure(errorMessage!!)
                    }
                }
            },
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text(if (isSignUp) "Sign Up" else "Login")
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = {
            isSignUp = !isSignUp
            errorMessage = null
        }) {
            Text(
                if (isSignUp)
                    "Already have an account? Login"
                else
                    "Don't have an account? Register"
            )
        }
    }
}
