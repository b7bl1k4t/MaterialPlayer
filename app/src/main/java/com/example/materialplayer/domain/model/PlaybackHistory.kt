package com.example.materialplayer.domain.model

data class PlaybackHistory(
    val id: Long,
    val trackId: Long,
    val playedAt: Long
)