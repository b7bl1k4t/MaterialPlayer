package com.example.materialplayer.data.repository

import com.example.materialplayer.data.local.dao.PlaybackHistoryDao
import com.example.materialplayer.data.local.entity.PlaybackHistoryEntity
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.data.mappers.toEntity
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomHistoryRepository @Inject constructor(
    private val historyDao: PlaybackHistoryDao
) : HistoryRepository {

    override fun recentTracks(limit: Int): Flow<List<Track>> =
        historyDao.recentTracks(limit).map { list -> list.map { it.toDomain() } }

    override fun mostPlayed(limit: Int): Flow<List<Track>> =
        historyDao.mostPlayedTracks(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun add(track: Track) {
        historyDao.insert(PlaybackHistoryEntity(
            trackId = track.id,
            playedAt = System.currentTimeMillis()
        ))
    }
}
