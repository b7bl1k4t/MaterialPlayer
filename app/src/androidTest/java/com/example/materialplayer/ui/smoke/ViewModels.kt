package com.example.materialplayer.ui.smoke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.materialplayer.data.local.dao.TrackDao
import com.example.materialplayer.domain.model.Playlist
import com.example.materialplayer.domain.model.PlaybackHistory
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.HistoryRepository
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TitleListViewModel @Inject constructor(
    private val repo: LibraryRepository,
    private val historyRepo: HistoryRepository,
    private val trackDao: TrackDao
) : ViewModel() {
    fun tracksByTitle(): Flow<PagingData<Track>> =
        repo.tracksByTitle().cachedIn(viewModelScope)

    fun onTrackClicked(trackId: Long) {
        viewModelScope.launch {
            historyRepo.recordPlay(trackId)
            trackDao.incrementPlayCount(trackId)
        }
    }
}


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: HistoryRepository
) : ViewModel() {
    fun recentPlays(): Flow<List<PlaybackHistory>> =
        repo.recentPlays()
}

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    private val repo: PlaylistRepository
) : ViewModel() {
    fun allPlaylists(): Flow<List<Playlist>> = repo.allPlaylists()
    fun createPlaylist(title: String) {
        viewModelScope.launch { repo.savePlaylist(Playlist(id = 0, title = title, createdAt = System.currentTimeMillis())) }
    }
}
