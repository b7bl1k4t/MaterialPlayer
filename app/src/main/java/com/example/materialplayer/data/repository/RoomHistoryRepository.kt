package com.example.materialplayer.data.repository

import com.example.materialplayer.data.local.dao.PlaybackHistoryDao
import com.example.materialplayer.data.local.entity.PlaybackHistoryEntity
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomHistoryRepository @Inject constructor(
    private val historyDao: PlaybackHistoryDao
) : HistoryRepository {

    override fun recentPlays(limit: Int) =
        historyDao.recent(limit).map { list -> list.map(PlaybackHistoryEntity::toDomain) }

    override suspend fun recordPlay(trackId: Long) {
        historyDao.insert(PlaybackHistoryEntity(trackId = trackId, playedAt = System.currentTimeMillis()))
    }
}