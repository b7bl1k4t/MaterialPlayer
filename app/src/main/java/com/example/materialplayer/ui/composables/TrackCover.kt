package com.example.materialplayer.ui.composables

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.R

@Composable
fun TrackCover(track: Track) {
    val context = LocalContext.current
    val data = remember(track.id) {
        track.coverUri                // уже есть в базе?
            ?: track.filePath
                .takeIf { it.scheme == "file" }          // 🟢 вместо startsWith
                ?.let { uri ->
                    MediaMetadataRetriever().run {
                        setDataSource(context, uri)
                        val art = embeddedPicture          // ByteArray?
                        release()
                        art
                    }
                }
    }

    AsyncImage(
        model = data ?: R.drawable.placeholder,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
    )
}