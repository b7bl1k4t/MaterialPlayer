package com.example.materialplayer.data.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.materialplayer.data.local.entity.PlaylistEntity
import com.example.materialplayer.data.local.entity.PlaylistTrackCrossRefEntity
import com.example.materialplayer.data.local.entity.TrackEntity

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id", // PK в PlaylistEntity
        entityColumn = "id", // PK в TrackEntity
        associateBy = Junction(
            value = PlaylistTrackCrossRefEntity::class,
            parentColumn = "playlist_id", // FK-колонка в cross-ref
            entityColumn = "track_id" // FK-колонка в cross-ref
        )
    )
    val tracks: List<TrackEntity> // все треки этого плейлиста
)