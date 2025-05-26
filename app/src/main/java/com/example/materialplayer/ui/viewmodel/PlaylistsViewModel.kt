package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.HistoryRepository
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val playlistRepo: PlaylistRepository,
    private val historyRepo: HistoryRepository,
    private val trackRepo: LibraryRepository
) : ViewModel() {

    enum class Tab { User, History, MostPlayed }

    private val _tab = MutableStateFlow(Tab.User)
    val tab: StateFlow<Tab> = _tab
    fun select(tab: Tab) { _tab.value = tab }

    /** Вызывается при клике на трек: инкремент + запуск воспроизведения */
    fun onTrackClick(track: Track) {
        viewModelScope.launch {
            trackRepo.incrementPlayCount(track.id)
        }
    }

    // данные под каждую вкладку
    val userPlaylists = playlistRepo.allPlaylists()

    val historyTracks = historyRepo.recentTracks()
    val mostPlayed = historyRepo.mostPlayed()
}
