package com.example.materialplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.materialplayer.data.local.dto.PlaylistWithTracks
import com.example.materialplayer.data.local.entity.PlaylistEntity
import com.example.materialplayer.data.local.entity.PlaylistTrackCrossRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    /* сами плейлисты */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaylist(entity: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("SELECT * FROM playlists ORDER BY created_at DESC")
    fun allPlaylists(): Flow<List<PlaylistEntity>>

    /* связи треков с плейлистами */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(cross: PlaylistTrackCrossRefEntity)

    @Query("""
        DELETE FROM playlist_track_cross_ref
        WHERE playlist_id = :playlistId AND track_id = :trackId
    """)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Transaction
    @Query("""
        SELECT * FROM playlists
        WHERE id = :id
    """)
    fun getPlaylistWithTracks(id: Long): Flow<PlaylistWithTracks>

}