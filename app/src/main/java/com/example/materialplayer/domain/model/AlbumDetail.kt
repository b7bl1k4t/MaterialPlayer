package com.example.materialplayer.domain.model

data class AlbumDetail(
    val id: Long,
    val title: String,
    val coverUri: String?,
    val tracks: List<Track>
)