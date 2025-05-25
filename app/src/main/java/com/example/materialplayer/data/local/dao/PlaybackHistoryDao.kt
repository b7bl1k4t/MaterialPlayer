package com.example.materialplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.materialplayer.data.local.entity.PlaybackHistoryEntity
import com.example.materialplayer.data.local.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybackHistoryDao {

    @Insert
    suspend fun insert(event: PlaybackHistoryEntity)

    @Transaction
    @Query("""
    SELECT tr.*
    FROM   playback_history ph
    JOIN   tracks           tr ON tr.id = ph.track_id
    ORDER  BY ph.played_at DESC
    LIMIT  :limit
    """)
    fun recentTracks(limit: Int): Flow<List<TrackEntity>>

    @Transaction
    @Query("""
    SELECT tr.*, COUNT(*) AS play_count
    FROM   playback_history ph
    JOIN   tracks           tr ON tr.id = ph.track_id
    GROUP  BY ph.track_id
    ORDER  BY play_count DESC
    LIMIT  :limit
    """)
    fun mostPlayedTracks(limit: Int): Flow<List<TrackEntity>>
}