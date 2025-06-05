package com.example.materialplayer.data.player

import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaybackRepository {
    /* read-only observable state */
    val currentTrack: StateFlow<Track?>
    val isPlaying: StateFlow<Boolean>
    val positionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>

    /* основной API */
    fun play(track: Track, queue: List<Track> = listOf(track))
    fun onCleared()
    fun pause()
    fun resume()
    fun seekTo(posMs: Long)
    fun skipToNext()
    fun skipToPrevious()
}