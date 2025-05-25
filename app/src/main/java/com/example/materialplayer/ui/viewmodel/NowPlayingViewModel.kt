package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.materialplayer.data.player.PlaybackConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val playbackConnection: PlaybackConnection
) : ViewModel() {
    val track = playbackConnection.currentTrack
    val isPlaying = playbackConnection.isPlaying
    val positionMs = playbackConnection.positionMs

    fun onPlayPause() {
        if (isPlaying.value) playbackConnection.pause()
        else playbackConnection.resume()
    }

    fun onSeek(position: Long) {
        playbackConnection.seekTo(position)
    }
}

