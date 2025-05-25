package com.example.materialplayer.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.materialplayer.data.local.entity.AlbumEntity

data class AlbumWithArtist(
    @Embedded val album: AlbumEntity,
    @ColumnInfo(name = "artistName") val artistName: String?
)
