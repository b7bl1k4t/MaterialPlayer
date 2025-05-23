package com.example.materialplayer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playback_history")
data class PlaybackHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "track_id")
    val trackId: Long,
    @ColumnInfo(name = "played_at")
    val playedAt: Long              // timestamp воспроизведения
)