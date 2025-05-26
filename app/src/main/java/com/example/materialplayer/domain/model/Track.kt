package com.example.materialplayer.domain.model

data class Track(
    val id: Long,
    val filePath: String,
    val parentDir: String,
    val durationMs: Long,
    val title: String?,
    val artistId: Long?,
    val albumId: Long?,
    val artistName: String?,
    val albumName: String?,
    val genre: String?,
    val trackNumber: Int?
)