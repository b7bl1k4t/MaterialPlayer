package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.AlbumWithTracks
import com.example.materialplayer.domain.model.AlbumDetail

fun AlbumWithTracks.toDomain(): AlbumDetail = AlbumDetail(
    id      = album.id,
    title   = album.title,
    coverUri= album.coverUri,
    tracks  = tracks.map { it.toDomain() }
)