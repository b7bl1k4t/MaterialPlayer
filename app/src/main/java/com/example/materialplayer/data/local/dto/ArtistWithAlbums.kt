package com.example.materialplayer.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.materialplayer.data.local.entity.ArtistEntity
import com.example.materialplayer.data.local.entity.AlbumEntity

/**
 * «Artist» + все его «Album» из БД, подтягиваются одним запросом
 */
data class ArtistWithAlbums(
    @Embedded
    val artist: ArtistEntity,

    @Relation(
        parentColumn  = "id",    // PK из ArtistEntity
        entityColumn  = "artist_id"// FK в AlbumEntity
    )
    val albums: List<AlbumEntity>
)
