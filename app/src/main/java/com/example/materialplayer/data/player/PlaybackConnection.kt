package com.example.materialplayer.data.player

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.data.player.PlaybackRepository

@Singleton
class PlaybackConnection @Inject constructor(
    private val repo: PlaybackRepository
) {
    val currentTrack: StateFlow<Track?> get() = repo.currentTrack
    val isPlaying:    StateFlow<Boolean> get() = repo.isPlaying
    val positionMs:   StateFlow<Long> get() = repo.positionMs

    // Прокси для управления воспроизведением
    fun play(track: Track, queue: List<Track> = listOf(track)) = repo.play(track, queue)
    fun pause() = repo.pause()
    fun resume() = repo.resume()
    fun seekTo(position: Long) = repo.seekTo(position)
}
