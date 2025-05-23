package com.example.materialplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.materialplayer.data.local.entity.PlaybackHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybackHistoryDao {

    @Insert
    suspend fun insert(event: PlaybackHistoryEntity)

    @Query("""
        SELECT * FROM playback_history
        ORDER BY played_at DESC
        LIMIT :limit
    """)
    fun recent(limit: Int): Flow<List<PlaybackHistoryEntity>>
}