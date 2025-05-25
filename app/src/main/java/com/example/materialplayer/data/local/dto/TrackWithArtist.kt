package com.example.materialplayer.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.materialplayer.data.local.entity.TrackEntity
import com.example.materialplayer.data.local.entity.ArtistEntity

data class TrackWithArtist(
    @Embedded val track: TrackEntity,
    @Relation(
        parentColumn = "artist_id",
        entityColumn = "id"
    )
    val artist: ArtistEntity?          // null → «Unknown»
)