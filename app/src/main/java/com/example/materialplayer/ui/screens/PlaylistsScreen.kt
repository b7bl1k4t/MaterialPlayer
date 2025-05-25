package com.example.materialplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            items(listOf("History", "Most Played")) { option ->
                AssistChip(
                    onClick = { /* TODO */ },
                    label = { Text(option) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Text(
            text = "Playlists Content",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}