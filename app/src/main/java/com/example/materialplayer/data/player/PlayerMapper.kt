package com.example.materialplayer.data.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.util.displayName
import android.net.Uri

/** конвертирует доменную модель в Media3-MediaItem */
fun Track.toMediaItem(): MediaItem =
    MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(filePath)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title ?: filePath.displayName)
                .setArtist(artistName)
                .setArtworkUri(coverUri?.let(Uri::parse))
                .setIsPlayable(true)
                .setDurationMs(durationMs)
                .build()
        )
        .build()
