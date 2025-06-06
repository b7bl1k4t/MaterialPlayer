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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.R
import com.example.materialplayer.ui.composables.PlaylistMode
import com.example.materialplayer.ui.composables.PlaylistsModeRow
import com.example.materialplayer.ui.viewmodel.PlaybackHolder
import com.example.materialplayer.ui.viewmodel.PlaylistsViewModel
import com.example.materialplayer.util.displayName

@Composable
fun PlaylistsScreen(
    vm: PlaylistsViewModel = hiltViewModel(),
    nav: NavController
) {
//    val playlists by vm.userPlaylists.collectAsState(initial = emptyList())
    val history by vm.historyTracks.collectAsState(initial = emptyList())
    val mostPlayed by vm.mostPlayed.collectAsState(initial = emptyList())
    val playback = hiltViewModel<PlaybackHolder>().connection
    var mode by rememberSaveable { mutableStateOf(PlaylistMode.History) }

    Column(Modifier.fillMaxSize()) {
        PlaylistsModeRow(selected = mode, onSelect = { mode = it })

        val list = if (mode == PlaylistMode.History) history else mostPlayed

        if (list.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Пока пусто") }
        } else {
            LazyColumn {
                items(list) { track ->
                    ListItem(
                        headlineContent   = { Text(track.title ?: "Unknown") },
                        supportingContent = { Text(track.artistName ?: "") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                playback.play(track, list)      // любой твой способ
                                nav.navigate("nowPlaying")
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
