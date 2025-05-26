package com.example.materialplayer.data.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.data.player.PlaybackRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlaybackRepository @Inject constructor(
    val player: ExoPlayer,                    // Hilt даёт из модуля
    @ApplicationContext ctx: Context
) : PlaybackRepository {

    private val _current = MutableStateFlow<Track?>(null)
    private val _playing = MutableStateFlow(false)
    private val _posMs = MutableStateFlow(0L)

    override val currentTrack: StateFlow<Track?> = _current.asStateFlow()
    override val isPlaying: StateFlow<Boolean> = _playing.asStateFlow()
    override val positionMs: StateFlow<Long> = _posMs.asStateFlow()

    init {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(item: MediaItem?, reason: Int) {
                _current.value = item?.mediaId?.toLongOrNull()?.let { id ->
                    // получите трек из БД если нужно, пока хватит «заглушки»
                    Track(
                        id,
                        item.mediaMetadata.title.toString(),
                        item.mediaMetadata.artist.toString(),
                        item.mediaMetadata.durationMs ?: 0L,
                        null, null, null, null, null, null, null,
                    )
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playing.value = isPlaying
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                _posMs.value = newPosition.positionMs
            }
        })
    }

    override fun play(track: Track, queue: List<Track>) {
        player.setMediaItems(queue.map { it.toMediaItem() })
        player.prepare()
        val index = queue.indexOf(track).coerceAtLeast(0)
        player.seekToDefaultPosition(index)
        player.play()
    }

    override fun pause() = player.pause()
    override fun resume() = player.play()
    override fun seekTo(posMs: Long) = player.seekTo(posMs)
}
