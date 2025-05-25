package com.example.materialplayer.data.player

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.materialplayer.domain.model.Track

/** конвертирует доменную модель в Media3-MediaItem */
fun Track.toMediaItem(): MediaItem =
    MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(filePath)              // в TrackEntity должно быть поле filePath / uri
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title ?: filePath.substringAfterLast('/'))
                .setArtist(artistName)
                .build()
        )
        .build()
