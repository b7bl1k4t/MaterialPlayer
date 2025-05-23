package com.example.materialplayer.ui.smoke

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button

@Composable
fun PlaylistListScreen(vm: PlaylistListViewModel = hiltViewModel()) {
    var newTitle by remember { mutableStateOf("") }
    val list by vm.allPlaylists().collectAsState(initial = emptyList())

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                placeholder = { Text("New playlist") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (newTitle.isNotBlank()) {
                    vm.createPlaylist(newTitle.trim())
                    newTitle = ""
                }
            }) {
                Text("Add")
            }
        }
        Spacer(Modifier.height(8.dp))
        // Горизонтальный список плейлистов
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { pl ->
                Card(Modifier.wrapContentWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(pl.title)
                    }
                }
            }
        }
    }
}

