package com.example.materialplayer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.materialplayer.R
import com.example.materialplayer.domain.model.AlbumSummary
import com.example.materialplayer.domain.model.Artist
import com.example.materialplayer.domain.model.FolderItem
import com.example.materialplayer.domain.model.Track

@Composable
fun FolderView(
    items: List<FolderItem>,
    onFolder: (FolderItem) -> Unit,
    onTrack: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = items,
            key = { it.path }
        ) { item ->
            ListItem(
                leadingContent = {
                    val icon = if (item.isFolder) R.drawable.ic_folder
                    else R.drawable.baseline_music_note_24
                    Icon(
                        painterResource(icon),
                        contentDescription = null
                    )
                },
                headlineContent = {
                    if (item.isFolder) {
                        Text(item.name ?: "Без названия")
                    } else {
                        Text(item.path.substringAfterLast('/'))
                    }
                },

                supportingContent = {
                    if (item.isFolder) {
                        Text("${item.subfolderCount} подпапок · ${item.trackCount} треков")
                    }
                },
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .clickable {
                        if (item.isFolder) onFolder(item)
                    }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
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
