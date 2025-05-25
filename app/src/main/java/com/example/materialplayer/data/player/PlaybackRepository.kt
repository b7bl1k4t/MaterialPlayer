package com.example.materialplayer.data.player

import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaybackRepository {
    /* read-only observable state */
    val currentTrack: StateFlow<Track?>
    val isPlaying: StateFlow<Boolean>
    val positionMs: StateFlow<Long>

    /* основной API */
    fun play(track: Track, queue: List<Track> = listOf(track))
    fun pause()
    fun resume()
    fun seekTo(posMs: Long)
}