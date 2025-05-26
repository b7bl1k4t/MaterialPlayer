package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.domain.model.PlaylistDetail
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    repo: PlaylistRepository,
    private val repoTrack: LibraryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    /** Вызывается при клике на трек: инкремент + запуск воспроизведения */
    fun onTrackClick(track: Track) {
        viewModelScope.launch {
            repoTrack.incrementPlayCount(track.id)
        }
    }

    private val id: Long = savedStateHandle["id"]!!

    val state: StateFlow<PlaylistDetail?> =
        repo.playlistDetail(id)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
}
