package com.example.materialplayer.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.materialplayer.R
import com.example.materialplayer.domain.model.AlbumSummary
import com.example.materialplayer.domain.model.Artist
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.Track

/**
 * Общий список «папки + файлы».
 */
@Composable
fun FolderView(
    items: List<BrowserItem>,
    onFolder: (BrowserItem.Folder) -> Unit,
    onTrack:  (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        /* ───────────────────────────────────────────── */
        items(items, key = { it.path }) { item ->
            when (item) {
                is BrowserItem.Folder -> ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.ic_folder), null) },
                    headlineContent = { Text(item.name) },
                    supportingContent = {
                        Text("${item.subfolderCount} подпапок · ${item.trackCount} треков")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFolder(item) }
                )

                is BrowserItem.TrackEntry -> ListItem(
                    leadingContent = {
                        Icon(painterResource(R.drawable.baseline_music_note_24), null)
                    },
                    headlineContent   = { Text(item.track.title ?: item.name) },
                    supportingContent = { Text(item.track.artistName ?: "Unknown") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTrack(item.track) }
                )
            }
            HorizontalDivider()
        }
        /* ───────────────────────────────────────────── */
    }
}


@Composable
fun TitleListView(list: List<Track>) {
    LazyColumn {
        items(list) { track ->
            ListItem(
                headlineContent = { Text(track.title ?: "Unknown") },
                supportingContent = { Text(track.artistName ?: "Unknown") }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun AlbumListView(
    albums: List<Pair<AlbumSummary, String>>,
    onClick: (AlbumSummary) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(albums) { (alb, artist) ->
            ListItem(
                leadingContent = {
                    Icon(
                        painterResource(R.drawable.outline_album_24),
                        contentDescription = null
                    )
                },
                headlineContent = { Text(alb.title, maxLines = 1) },
                supportingContent = { Text(artist, maxLines = 1) },
                modifier = Modifier.clickable { onClick(alb) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun ArtistListView(list: List<Artist>, onClick: (Artist) -> Unit) {
    LazyColumn {
        items(list) { ar ->
            ListItem(
                headlineContent = { Text(ar.name) },
                modifier = Modifier.clickable { onClick(ar) }
            )
            HorizontalDivider()
        }
    }
}
