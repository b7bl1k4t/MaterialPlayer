package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigator
import com.example.materialplayer.data.player.PlaybackConnection
import com.example.materialplayer.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewModel @Inject constructor(
    private val playback: PlaybackConnection
) : ViewModel() {

    /** объединяем три потока из плеера */
    val uiState: StateFlow<UiState> = combine(
        playback.currentTrack,          // Flow<Track?>
        playback.positionMs,            // Flow<Long>
        playback.durationMs,            // Flow<Long>
        playback.isPlaying              // Flow<Boolean>
    ) { track, pos, dur, playing ->
        UiState(
            currentTrack = track,
            progressFraction = if (dur > 0) pos.toFloat() / dur else 0f,
            isPlaying = playing
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    /* ——— callbacks ——— */
    fun onPlayPause() {
        if (uiState.value.isPlaying) playback.pause() else playback.resume()
    }

    fun onNext() = playback.next()
}

/** light POJO for UI  */
data class UiState(
    val currentTrack: Track? = null,
    val progressFraction: Float = 0f,
    val isPlaying: Boolean = false
)
