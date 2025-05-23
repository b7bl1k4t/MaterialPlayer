package com.example.materialplayer.domain.repository

import com.example.materialplayer.domain.model.Playlist
import com.example.materialplayer.domain.model.PlaylistDetail
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun allPlaylists(): Flow<List<Playlist>>
    suspend fun savePlaylist(pl: Playlist)
    suspend fun deletePlaylist(id: Long)
    fun playlistDetail(id: Long): Flow<PlaylistDetail>
    suspend fun addTrack(plId: Long, trackId: Long, order: Int)
    suspend fun removeTrack(plId: Long, trackId: Long)
}