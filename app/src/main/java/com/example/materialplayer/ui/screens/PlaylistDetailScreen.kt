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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    id: Long,
    nav: NavController,
    vm: PlaylistDetailViewModel = hiltViewModel()
) {
    val data by vm.state.collectAsState()

    data?.let { pl ->
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = { Text(pl.playlist.title) },
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
                items(pl.tracks) { t ->
                    ListItem(
                        headlineContent = { Text(t.filePath.substringAfterLast('/')) },
                        supportingContent = { Text(t.artistName ?: "Unknown") },
                        leadingContent = {
                            Icon(painterResource(R.drawable.baseline_music_note_24), null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* play track */ }
                    )
                    HorizontalDivider()
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
