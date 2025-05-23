package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.ArtistEntity
import com.example.materialplayer.domain.model.Artist

fun ArtistEntity.toDomain() = Artist(
    id = id,
    name = name
)

fun Artist.toEntity() = ArtistEntity(
    id = id,
    name = name
)
