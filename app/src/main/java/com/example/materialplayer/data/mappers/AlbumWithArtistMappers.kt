package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.AlbumWithArtist
import com.example.materialplayer.domain.model.AlbumSummary

fun AlbumWithArtist.toSummary() = AlbumSummary(
    id = album.id,
    title = album.title,
    coverUri = album.coverUri
) to (artistName ?: "Unknown")