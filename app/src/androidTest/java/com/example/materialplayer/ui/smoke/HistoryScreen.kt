package com.example.materialplayer.ui.smoke

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryScreen(vm: HistoryViewModel = hiltViewModel()) {
    val events by vm.recentPlays().collectAsState(initial = emptyList())

    // Горизонтальный список истории
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    ) {
        items(events) { event ->
            Card(Modifier.wrapContentWidth()) {
                Text(
                    text = "#${event.trackId} at ${event.playedAt}",
                    Modifier.padding(12.dp)
                )
            }
        }
    }
}
