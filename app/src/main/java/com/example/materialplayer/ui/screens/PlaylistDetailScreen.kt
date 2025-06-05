package com.example.materialplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.ui.viewmodel.PlaylistDetailViewModel
import com.example.materialplayer.R
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.ui.viewmodel.PlaybackHolder
import com.example.materialplayer.util.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    nav: NavController,
    vm: PlaylistDetailViewModel = hiltViewModel(),
) {
    val data by vm.state.collectAsState()
    val playback = hiltViewModel<PlaybackHolder>().connection

    data?.let { playlist ->
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = { Text(playlist.playlist.title) },
                    navigationIcon = {
                        IconButton({ nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(playlist.tracks) { track ->
                    ListItem(
                        headlineContent = { Text(track.filePath.displayName) },
                        supportingContent = { Text(track.artistName ?: "Unknown") },
                        leadingContent = {
                            Icon(painterResource(R.drawable.baseline_music_note_24), null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                vm.onTrackClick(track)
                                playback.play(track, playlist.tracks)
                                nav.navigate("nowPlaying")
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
