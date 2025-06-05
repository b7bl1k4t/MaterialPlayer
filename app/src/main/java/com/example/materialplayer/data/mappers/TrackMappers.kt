package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.TrackEntity
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.util.displayName

/* Entity -> Domain */
fun TrackEntity.toDomain() = Track(
    id = id,
    filePath = filePath,
    parentDir = parentDir,
    title = title,
    artistId = artistId,
    albumId = albumId,
    artistName = artistName,
    albumName = albumName,
    coverUri = coverUri,
    genre = genre,
    trackNumber = trackNumber,
    durationMs = durationMs
)

fun TrackEntity.toBrowserItem() = BrowserItem.TrackEntry(
    path = filePath.toString(),
    name = filePath.displayName,
    track = toDomain()                   // ваш mapper TrackEntity → Track
)

/* Domain -> Entity */
fun Track.toEntity() = TrackEntity(
    id = id,
    filePath = filePath,
    parentDir = parentDir,
    playCount = 0,
    title = title,
    artistId = artistId,
    albumId = albumId,
    artistName = artistName,
    albumName = albumName,
    coverUri = coverUri,
    genre = genre,
    trackNumber = trackNumber,
    durationMs = durationMs
)