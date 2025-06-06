package com.example.materialplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.wear.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.materialplayer.ui.viewmodel.NowPlayingViewModel
import com.example.materialplayer.R
import com.example.materialplayer.data.local.entity.TrackEntity
import com.example.materialplayer.ui.composables.TrackCover
import com.example.materialplayer.util.displayName
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    onBack: () -> Unit,
    viewModel: NowPlayingViewModel = hiltViewModel()
) {
    val trackState by viewModel.track.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val position by viewModel.positionMs.collectAsState()
    val duration by viewModel.durationMs.collectAsState()

    var dragging by remember { mutableStateOf(false) }
    var dragPos  by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Album art
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ){
                TrackCover(trackState!!)
            }

            Spacer(Modifier.height(24.dp))

            // Track title
            Text(
                text = trackState?.title.orEmpty()
                    .ifBlank { trackState?.filePath?.displayName ?: "unknown" },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )

            Spacer(Modifier.height(4.dp))

            // Artist name
            Text(
                text = trackState?.artistName.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // Playback controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = viewModel::onPrev,
                    onLongClick = { viewModel.onSeek(max(0L, position - 10_000L)) },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        painterResource(R.drawable.outline_skip_previous_24),
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(Modifier.width(12.dp))

                IconButton(
                    onClick = { viewModel.onPlayPause() },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .weight(1f)
                ) {
                    Icon(
                        if (isPlaying) painterResource(R.drawable.outline_pause_24) else painterResource(R.drawable.outline_play_arrow_24),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(Modifier.width(12.dp))

                IconButton(
                    onClick = viewModel::onNext,
                    onLongClick = { viewModel.onSeek(position + 10_000L)  },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        painterResource(R.drawable.outline_skip_next_24),
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Progress slider + times
            Column {
                Slider(
                    value = if (dragging) dragPos else viewModel.positionFraction.collectAsState().value,
                    onValueChange = { v ->
                        dragPos  = v
                        dragging = true
                    },
                    onValueChangeFinished = {
                        viewModel.seekToFraction(dragPos)
                        dragging = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(position),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
