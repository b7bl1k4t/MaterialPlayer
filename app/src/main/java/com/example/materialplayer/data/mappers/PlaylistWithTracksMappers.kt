package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.PlaylistWithTracks
import com.example.materialplayer.domain.model.Playlist
import com.example.materialplayer.domain.model.PlaylistDetail

fun PlaylistWithTracks.toDomain(): PlaylistDetail {
    val domainPlaylist = Playlist(
        id = playlist.id,
        title = playlist.title,
        createdAt = playlist.createdAt
    )
    val domainTracks = tracks.map { it.toDomain() } //TrackEntity.toDomain()
    return PlaylistDetail(domainPlaylist, domainTracks)
}
