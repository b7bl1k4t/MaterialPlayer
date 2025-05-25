package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.materialplayer.domain.repository.HistoryRepository
import com.example.materialplayer.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val plRepo: PlaylistRepository,
    private val histRepo: HistoryRepository
) : ViewModel() {

    enum class Tab { User, History, MostPlayed }

    private val _tab = MutableStateFlow(Tab.User)
    val tab: StateFlow<Tab> = _tab
    fun select(tab: Tab) { _tab.value = tab }

    // данные под каждую вкладку
    val userPlaylists = plRepo.allPlaylists()

    val historyTracks = histRepo.recentTracks()
    val mostPlayed = histRepo.mostPlayed()
}
