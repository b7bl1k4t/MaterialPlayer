package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.ArtistWithAlbums
import com.example.materialplayer.domain.model.AlbumSummary
import com.example.materialplayer.domain.model.ArtistDetail

fun ArtistWithAlbums.toDomain(): ArtistDetail = ArtistDetail(
    id = artist.id,
    name = artist.name,
    albums = albums.map { AlbumSummary(
        id = it.id,
        title = it.title,
        coverUri= it.coverUri
    ) }
)