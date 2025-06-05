package com.example.materialplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.R
import com.example.materialplayer.ui.viewmodel.PlaybackHolder
import com.example.materialplayer.ui.viewmodel.SearchItem
import com.example.materialplayer.ui.viewmodel.SearchViewModel
import com.example.materialplayer.util.displayName

@Composable
fun SearchScreen(
    nav: NavController,
    vm: SearchViewModel = hiltViewModel()
) {
    val query by vm.query.collectAsState()
    val items by vm.results.collectAsState()
    val playback = hiltViewModel<PlaybackHolder>().connection

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = vm::update,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search…") },
            singleLine  = true,
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )

        LazyColumn {
            items(items) {
                when (it) {
                    is SearchItem.Track -> ListItem(
                        headlineContent = { Text(it.track.filePath.displayName) },
                        supportingContent = { Text(it.artist) },
                        leadingContent = { Icon(
                            painterResource(R.drawable.baseline_music_note_24),
                            contentDescription = null
                        ) },
                        modifier = Modifier.clickable {
                            vm.onTrackClick(it.track)
                            playback.play(it.track, (items.filterIsInstance<SearchItem.Track>())
                                    .map { t -> t.track })   // очередь = все найденные
                            nav.navigate("nowPlaying")
                        }
                    )

                    is SearchItem.Album -> ListItem(
                        headlineContent = { Text(it.album.title) },
                        supportingContent = { Text(it.artist) },
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.outline_album_24),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable {
                            nav.navigate("album/${it.album.id}")
                        }
                    )

                    is SearchItem.Artist -> ListItem(
                        headlineContent = { Text(it.artist.name) },
                        leadingContent = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.clickable {
                            nav.navigate("artist/${it.artist.id}")
                        }
                    )
                }
                HorizontalDivider()
            }
        }
    }
}

