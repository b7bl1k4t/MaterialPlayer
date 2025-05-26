package com.example.materialplayer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["album_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("parent_dir"),
        Index("title"),
        Index("artist_id"),
        Index("album_id"),
        Index("play_count")
    ]
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "file_path")
    val filePath: String,
    @ColumnInfo(name = "parent_dir")
    val parentDir: String,          // полный путь к папке
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long,
    @ColumnInfo(name = "play_count")
    val playCount: Int,

    @ColumnInfo(name = "artist_id")
    val artistId: Long?, // FK → artists
    @ColumnInfo(name = "album_id")
    val albumId: Long?, // FK → albums

    // ID3 основные теги
    val title: String?,             // TIT2
    @ColumnInfo(name = "artist_name")
    val artistName: String?,
    @ColumnInfo(name = "album_name")
    val albumName: String?,
    val genre: String?,
    @ColumnInfo(name = "track_no")
    val trackNumber: Int?,          // TRCK
)

