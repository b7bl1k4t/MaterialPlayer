package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.entity.PlaybackHistoryEntity
import com.example.materialplayer.domain.model.PlaybackHistory

fun PlaybackHistoryEntity.toDomain() = PlaybackHistory(
    id = id,
    trackId = trackId,
    playedAt = playedAt
)

fun PlaybackHistory.toEntity() = PlaybackHistoryEntity(
    id = id,
    trackId = trackId,
    playedAt = playedAt
)
