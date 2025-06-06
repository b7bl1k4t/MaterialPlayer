package com.example.materialplayer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.materialplayer.R

/**
 * Mini player bar with progress‑fill background.
 * @param trackTitle    title of the current track
 * @param artistName    artist name
 * @param cover         any Coil‑compatible data (Uri/String/ByteArray); `null` → placeholder
 * @param progress      [0f‥1f] fraction of playback progress
 * @param isPlaying     player state for Play/Pause toggle
 * @param onPlayPause   callback for play/pause tap
 * @param onNext        callback for skip‑next
 * @param onClick       callback when bar itself tapped (open NowPlaying)
 * @param modifier      external modifier
 */
@Composable
fun MiniPlayerBar(
    trackTitle: String,
    artistName: String,
    cover: Any? = null,
    progress: Float,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    val progressClamped = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor)
            .drawBehind {
                // draw filled rect for progress
                val w = size.width * progressClamped
                if (w > 0f) {
                    drawRect(color = progressColor.copy(alpha = 0.25f), size = Size(w, size.height))
                }
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cover)
                    .placeholder(R.drawable.placeholder)
                    .fallback(R.drawable.placeholder)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = trackTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                IconButton(onClick = onPlayPause) {
                    Icon(
                        painter = painterResource(if (isPlaying) R.drawable.outline_pause_24 else R.drawable.outline_play_arrow_24),
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                IconButton(onClick = onNext) {
                    Icon(painterResource(R.drawable.outline_skip_next_24), contentDescription = "Next")
                }
            }
        }
    }
}

@Preview("Mini Player – Light", showBackground = true)
@Composable
private fun MiniBarPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        MiniPlayerBar(
            trackTitle = "Fire Coming Out Of The Monkey's Head",
            artistName = "Gorillaz",
            cover = null,
            progress = 0.45f,
            isPlaying = true,
            onPlayPause = {},
            onNext = {}
        )
    }
}
