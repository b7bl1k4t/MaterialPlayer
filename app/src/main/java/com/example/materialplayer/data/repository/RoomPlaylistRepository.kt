package com.example.materialplayer.data.repository

import com.example.materialplayer.data.local.dao.PlaylistDao
import com.example.materialplayer.data.local.entity.PlaylistEntity
import com.example.materialplayer.data.local.entity.PlaylistTrackCrossRefEntity
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.data.mappers.toEntity
import com.example.materialplayer.domain.model.Playlist
import com.example.materialplayer.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomPlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override fun allPlaylists() =
        playlistDao.allPlaylists().map { list -> list.map(PlaylistEntity::toDomain) }

    override suspend fun savePlaylist(pl: Playlist) =
        playlistDao.savePlaylist(pl.toEntity())

    override suspend fun deletePlaylist(id: Long) =
        playlistDao.deletePlaylist(id)

    override fun playlistDetail(id: Long) =
        playlistDao.getPlaylistWithTracks(id).map { it.toDomain() }

    override suspend fun addTrack(plId: Long, trackId: Long, order: Int) =
        playlistDao.addTrackToPlaylist(PlaylistTrackCrossRefEntity(plId, trackId, order))

    override suspend fun removeTrack(plId: Long, trackId: Long) =
        playlistDao.removeTrackFromPlaylist(plId, trackId)
}