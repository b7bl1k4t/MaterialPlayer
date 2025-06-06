package com.example.materialplayer.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.materialplayer.data.player.toMediaItem
import com.example.materialplayer.ui.viewmodel.MiniPlayerViewModel

@Composable
fun MiniPlayerHost(
    navController: NavController,
    viewModel: MiniPlayerViewModel = hiltViewModel(),
    nowPlayingRoute: String = "nowPlaying"
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute == nowPlayingRoute) return

    val ui by viewModel.uiState.collectAsState()

    ui.currentTrack?.let { track ->
        MiniPlayerBar(
            trackTitle = track.title ?: "Unknown",
            artistName = track.artistName ?: "",
            cover = track.coverUri ?: track.toMediaItem().mediaMetadata.artworkUri,
            progress = ui.progressFraction,
            isPlaying = ui.isPlaying,
            onPlayPause = viewModel::onPlayPause,
            onNext = viewModel::onNext,
            onClick = { navController.navigate("nowPlaying") }
        )
    }
}
