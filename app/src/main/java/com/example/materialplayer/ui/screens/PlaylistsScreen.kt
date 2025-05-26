package com.example.materialplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.R
import com.example.materialplayer.ui.viewmodel.PlaylistsViewModel

@Composable
fun PlaylistsScreen(
    vm: PlaylistsViewModel = hiltViewModel(),
    nav: NavController
) {
    val tab by vm.tab.collectAsState()
    val playlists by vm.userPlaylists.collectAsState(initial = emptyList())
    val history by vm.historyTracks.collectAsState(initial = emptyList())
    val mostPlayed by vm.mostPlayed.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            /* ─────────── верхняя панель ─────────── */
            val tabs = listOf(
                "Playlists" to PlaylistsViewModel.Tab.User,
                "History" to PlaylistsViewModel.Tab.History,
                "Most Played" to PlaylistsViewModel.Tab.MostPlayed
            )

            LazyRow(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                items(tabs) { (label, value) ->
                    AssistChip(
                        onClick = { vm.select(value) },
                        label = { Text(label) },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        colors = if (tab == value)
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        else AssistChipDefaults.assistChipColors()
                    )
                }
            }

            /* ─────────── содержимое ─────────── */
            when (tab) {
                PlaylistsViewModel.Tab.User -> {
                    LazyColumn {
                        items(playlists) { pl ->
                            ListItem(
                                headlineContent = { Text(pl.title) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { nav.navigate("playlist/${pl.id}") }
                            )
                            HorizontalDivider()
                        }
                    }
                }

                PlaylistsViewModel.Tab.History -> {
                    LazyColumn {
                        items(history) { t ->
                            ListItem(
                                headlineContent = { Text(t.filePath.substringAfterLast('/')) },
                                supportingContent = { Text(t.artistName ?: "Unknown") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            HorizontalDivider()
                        }
                    }
                }

                PlaylistsViewModel.Tab.MostPlayed -> {
                    LazyColumn {
                        items(mostPlayed) { t ->
                            ListItem(
                                leadingContent = { Icon(painterResource(R.drawable.outline_album_24), null) },
                                headlineContent = { Text(t.filePath.substringAfterLast('/')) },
                                supportingContent = { Text(t.artistName ?: "Unknown") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
        Button(
            onClick = { /* TODO: добавить функционал */ },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add playlist")
        }
    }


}
