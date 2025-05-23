package com.example.materialplayer.domain.model

data class ArtistDetail(
    val id: Long,
    val name: String,
    val albums: List<AlbumSummary>
)