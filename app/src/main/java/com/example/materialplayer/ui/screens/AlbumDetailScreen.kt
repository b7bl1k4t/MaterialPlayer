package com.example.materialplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.ui.viewmodel.PlaybackHolder
import com.example.materialplayer.ui.viewmodel.AlbumDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    nav: NavController,
    vm: AlbumDetailViewModel = hiltViewModel(),
) {
    val data by vm.state.collectAsState()
    val playback = hiltViewModel<PlaybackHolder>().connection
    data?.let { album ->
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(album.title) },
                navigationIcon = {
                    IconButton({ nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }) { padd ->
            LazyColumn(contentPadding = padd) {
                items(album.tracks) { track ->
                    ListItem(
                        headlineContent = { Text(track.title ?: "Unknown") },
                        modifier = Modifier.clickable {
                            vm.onTrackClick(track)
                            playback.play(track, album.tracks)
                            nav.navigate("nowPlaying")
                        }
                    )
                }
            }
        }
    }
}