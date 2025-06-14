package com.example.materialplayer.ui.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class LibraryMode(val title: String) {
    Folder("Folder"),
    Title("Title"),
    Album("Album"),
    Artist("Artist")
}

enum class PlaylistMode(val title: String) {
    History("History"),
    MostPlayed("Most played")
}

@Composable
fun ModeRow(
    selected: LibraryMode,
    onSelect: (LibraryMode) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        LibraryMode.entries.forEach { mode ->
            FilterChip(
                selected = mode == selected,
                onClick = { onSelect(mode) },
                label = { Text(mode.title) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun PlaylistsModeRow(
    selected: PlaylistMode,
    onSelect: (PlaylistMode) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        PlaylistMode.entries.forEach { mode ->
            FilterChip(
                selected = mode == selected,
                onClick   = { onSelect(mode) },
                label     = { Text(mode.title) },
                modifier  = Modifier.padding(end = 8.dp)
            )
        }
    }
}
