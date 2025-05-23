package com.example.materialplayer.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.materialplayer.data.local.entity.AlbumEntity
import com.example.materialplayer.data.local.entity.TrackEntity

data class AlbumWithTracks(
    @Embedded val album: AlbumEntity,
    @Relation(
        parentColumn = "id", // PK из AlbumEntity
        entityColumn = "album_id" // FK в TrackEntity
    )
    val tracks: List<TrackEntity>
)