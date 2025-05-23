package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.PlaylistEntity
import com.example.materialplayer.domain.model.Playlist

fun PlaylistEntity.toDomain() = Playlist(
    id = id,
    title = title,
    createdAt = createdAt
)

fun Playlist.toEntity() = PlaylistEntity(
    id = id,
    title = title,
    createdAt  = createdAt
)
