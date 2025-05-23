package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.TrackEntity
import com.example.materialplayer.domain.model.Track

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
    genre = genre,
    trackNo = trackNo,
    durationMs = durationMs
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
    genre = genre,
    trackNo = trackNo,
    durationMs = durationMs
)
