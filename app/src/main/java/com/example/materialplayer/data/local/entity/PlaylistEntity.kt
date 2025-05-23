package com.example.materialplayer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: Long,
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long              // timestamp создания
)