package com.example.materialplayer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
    indices = [Index("name", unique = true)]
)
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)