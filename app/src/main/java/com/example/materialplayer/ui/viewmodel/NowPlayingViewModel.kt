package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.player.PlaybackConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val playbackConnection: PlaybackConnection
) : ViewModel() {
    val track = playbackConnection.currentTrack
    val isPlaying = playbackConnection.isPlaying
    val positionMs = playbackConnection.positionMs
    val durationMs = playbackConnection.durationMs

    val positionFraction = positionMs
        .combine(durationMs) { pos, dur ->
            if (dur > 0) pos.toFloat() / dur else 0f
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0f
        )

    fun onPlayPause() {
        if (isPlaying.value) playbackConnection.pause()
        else playbackConnection.resume()
    }

    fun onSeek(position: Long) {
        playbackConnection.seekTo(position)
    }

    fun onPrev() = playbackConnection.prev()
    fun onNext() = playbackConnection.next()
    fun seekToFraction(fraction: Float) {
        val pos = (fraction * durationMs.value).toLong()
        playbackConnection.seekTo(pos)
    }
}