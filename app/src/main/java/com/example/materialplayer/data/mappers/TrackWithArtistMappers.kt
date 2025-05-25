package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.TrackWithArtist

fun TrackWithArtist.toPair() =
    track.toDomain() to (artist?.name ?: "Unknown")