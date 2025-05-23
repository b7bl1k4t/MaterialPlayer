package com.example.materialplayer.domain.model

data class PlaylistDetail(
    val playlist: Playlist,
    val tracks: List<Track>
)