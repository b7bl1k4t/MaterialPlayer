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

@Composable
private fun ModeRow(
    selected: LibraryMode,
    onSelect: (LibraryMode) -> Unit
) {
    val scroll = rememberScrollState()
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(scroll)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        LibraryMode.entries.forEach { mode ->
            val isSel = mode == selected
            FilterChip(
                selected = isSel,
                onClick = { onSelect(mode) },
                label = { Text(mode.title) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
