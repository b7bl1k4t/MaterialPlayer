package com.example.materialplayer.domain.model

import android.net.Uri

data class Track(
    val id: Long,
    val filePath: Uri,
    val parentDir: String,
    val durationMs: Long,
    val title: String?,
    val artistId: Long?,
    val albumId: Long?,
    val artistName: String?,
    val albumName: String?,
    val coverUri: String?,
    val genre: String?,
    val trackNumber: Int?
)