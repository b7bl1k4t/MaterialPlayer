package com.example.materialplayer.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// — ЗАГЛУШКА, когда нет ни одного элемента
@Composable
fun LibraryPlaceholder(onNavigateToSettings: () -> Unit){
    Box(
        modifier = Modifier.Companion.fillMaxSize(),
        contentAlignment = Alignment.Companion.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.Companion.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Настройки",
                modifier = Modifier.Companion.size(64.dp)
            )
            Spacer(Modifier.Companion.height(16.dp))
            Text(
                text = "Библиотека пуста",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.Companion.height(8.dp))
            Text(
                text = "Добавьте корневую папку в настройках и просканируйте библиотеку",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.Companion.padding(horizontal = 16.dp),
                textAlign = TextAlign.Companion.Center
            )
            Spacer(Modifier.Companion.height(24.dp))
            Button(onClick = onNavigateToSettings) {
                Text("Перейти в настройки")
            }
        }
    }
}