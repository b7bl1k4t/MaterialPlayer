package com.example.materialplayer.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.ui.composables.AlbumListView
import com.example.materialplayer.ui.viewmodel.ArtistDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    nav: NavController,
    vm: ArtistDetailViewModel = hiltViewModel()
) {
    val data by vm.state.collectAsState()
    data?.let { artist ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(artist.name) },
                    navigationIcon = {
                        IconButton({ nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            AlbumListView(
                albums = artist.albums.map { it to artist.name },
                onClick = { nav.navigate("album/${it.id}") },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}