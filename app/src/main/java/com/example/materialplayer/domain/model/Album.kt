package com.example.materialplayer.domain.model

data class Album(
    val id: Long,
    val title: String,
    val artistId: Long?,
    val coverUri: String?
)