package com.example.materialplayer.ui.smoke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmokeTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    SmokeTestScreen()
                }
            }
        }
    }
}

@Composable
fun SmokeTestScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section 1: Tracks by Title
        item {
            Text("Tracks by Title", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            TitleListScreen()
        }

        // Section 2: Recently Played
        item {
            Text("Recently Played", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            HistoryScreen()
        }

        // Section 3: Playlists
        item {
            Text("Playlists", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            PlaylistListScreen()
        }
    }
}