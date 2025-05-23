package com.example.materialplayer.domain.repository

import com.example.materialplayer.domain.model.PlaybackHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun recentPlays(limit: Int = 50): Flow<List<PlaybackHistory>>
    suspend fun recordPlay(trackId: Long)
}