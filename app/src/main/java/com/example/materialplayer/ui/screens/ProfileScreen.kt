package com.example.materialplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.ui.navigation.Navigation
import com.example.materialplayer.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    nav: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user by authViewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (user != null) {
            Text(
                text = "Добро пожаловать, ${user!!.email}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.signOut()
                // После выхода возвращаемся назад
                nav.popBackStack(
                    route = Navigation.Library.route,
                    inclusive = false
                )
            }) {
                Text("Выйти")
            }
        } else {
            Text(
                text = "Вы не вошли в аккаунт",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Переход на экран аутентификации
                nav.navigate(Navigation.Auth.route)
            }) {
                Text("Войти / Зарегистрироваться")
            }
        }
    }
}
