package com.example.materialplayer.ui.screens

import com.example.materialplayer.ui.composables.*
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.ui.composables.LibraryMode
import com.example.materialplayer.ui.composables.LibraryPlaceholder
import com.example.materialplayer.ui.composables.ScrollableHeadline
import com.example.materialplayer.ui.navigation.Navigation
import com.example.materialplayer.ui.viewmodel.LibraryViewModel
import com.example.materialplayer.ui.viewmodel.PlaybackHolder

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),       // :contentReference[oaicite:0]{index=0}
    nav: NavController,
    onNavigateToSettings: () -> Unit
) {
    // React to data from the ViewModel
    val roots by viewModel.rootsFlow.collectAsState()
    val items by viewModel.itemsFlow.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val mode by viewModel.mode.collectAsState()
    val titles by viewModel.titles.collectAsState(initial = emptyList())
    val albums by viewModel.albums.collectAsState(initial = emptyList())
    val artists by viewModel.artists.collectAsState(initial = emptyList())
    val playback = hiltViewModel<PlaybackHolder>().connection
    val tracks = items.filterIsInstance<BrowserItem.TrackEntry>()

    // Enable system Back press only when we are inside a folder
    BackHandler(enabled = currentPath != null) {
        viewModel.onBack()
    }

    // Simple list without any sorting widgets for now

    Column(Modifier.fillMaxSize()) {
        ModeRow(selected = mode, onSelect = viewModel::setMode)
        HorizontalDivider()

        when (mode) {
            LibraryMode.Folder -> {
                when {
                    roots.isEmpty() && currentPath == null -> LibraryPlaceholder(onNavigateToSettings)
                    else -> {
                        Column {
                            // ─── ЗАГОЛОВОК ────────────────────────────────
                            val title = currentPath
                                ?.let { Uri.decode(it).substringAfterLast('/') }
                                ?: "Roots"

                            ScrollableHeadline(title)
                            FolderView(
                                items = items,
                                onFolder = { viewModel.onFolderClick(it.path) },
                                onTrack  = { track ->
                                    viewModel.onTrackClick(track)
                                    playback.play(track, tracks.map { it.track })
                                    nav.navigate(Navigation.NowPlaying.route)
                                }
                            )
                        }
                    }
                }
            }
            LibraryMode.Title -> TitleListView(titles)
            LibraryMode.Album -> AlbumListView(
                albums  = albums,
                onClick = { nav.navigate("album/${it.id}") }
            )
            LibraryMode.Artist -> ArtistListView(artists) {
                nav.navigate("artist/${it.id}")
            }
        }
    }
}