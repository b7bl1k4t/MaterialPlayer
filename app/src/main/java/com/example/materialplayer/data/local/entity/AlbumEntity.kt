package com.example.materialplayer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums",
    foreignKeys = [
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("title"), Index("artist_id")]
)
data class AlbumEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    @ColumnInfo(name = "artist_id")
    val artistId: Long?,       // FK → artists
    @ColumnInfo(name = "cover_uri")
    val coverUri: String?,          // локальная или внешняя обложка
)