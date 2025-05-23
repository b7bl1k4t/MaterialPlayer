package com.example.materialplayer.domain.model

data class Track(
    val id: Long,
    val filePath: String,
    val parentDir: String,
    val title: String?,
    val artistId: Long?,
    val albumId: Long?,
    val artistName: String?,
    val albumName: String?,
    val genre: String?,
    val trackNo: Int?,
    val durationMs: Int
)