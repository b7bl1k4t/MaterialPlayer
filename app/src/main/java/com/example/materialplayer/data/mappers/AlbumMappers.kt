package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.AlbumEntity
import com.example.materialplayer.domain.model.Album

fun AlbumEntity.toDomain() = Album(
    id = id,
    title = title,
    artistId = artistId,
    coverUri = coverUri
)

fun Album.toEntity() = AlbumEntity(
    id = id,
    title = title,
    artistId = artistId,
    coverUri = coverUri
)
