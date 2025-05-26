package com.example.materialplayer.domain.repository

import com.example.materialplayer.domain.model.PlaybackHistory
import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun recentTracks(limit: Int = 50): Flow<List<Track>>
    fun mostPlayed(limit: Int = 50):  Flow<List<Track>>
    suspend fun add(track: Track)
}