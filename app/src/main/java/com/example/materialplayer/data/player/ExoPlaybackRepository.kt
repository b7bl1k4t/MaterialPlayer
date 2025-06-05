package com.example.materialplayer.data.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.materialplayer.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


/** сколько миллисекунд считать «уже в процессе» */
private const val RESTART_THRESHOLD_MS = 2_000L

@Singleton
class ExoPlaybackRepository @Inject constructor(
    val player: ExoPlayer,                    // Hilt даёт из модуля
    @ApplicationContext ctx: Context
) : PlaybackRepository {

    private val _current = MutableStateFlow<Track?>(null)
    private val _playing = MutableStateFlow(false)
    private val _posMs = MutableStateFlow(0L)
    private val _durationMs = MutableStateFlow(0L)

    override val currentTrack: StateFlow<Track?> = _current.asStateFlow()
    override val isPlaying: StateFlow<Boolean> = _playing.asStateFlow()
    override val positionMs: StateFlow<Long> = _posMs.asStateFlow()
    override val durationMs: StateFlow<Long> = _durationMs
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(item: MediaItem?, reason: Int) {
                _current.value = item?.mediaId?.toLongOrNull()?.let { id ->
                    // получите трек из БД если нужно, пока хватит «заглушки»
                    Track(
                        id = id,
                        filePath = item.localConfiguration?.uri ?: Uri.EMPTY,
                        parentDir = item.mediaMetadata.artist.toString(),
                        coverUri = item.mediaMetadata.artworkUri.toString(),
                        durationMs = item.mediaMetadata.durationMs ?: 0L,
                        title = null,
                        artistId = null,
                        albumId = null,
                        artistName = null,
                        albumName = null,
                        genre = null,
                        trackNumber = null
                    )
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY || state == Player.STATE_BUFFERING) {
                    _durationMs.value = player.duration.coerceAtLeast(0L)
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

        scope.launch {
            while (isActive) {
                _posMs.value = player.currentPosition
                delay(500)          // 2× в секунду более чем достаточно
            }
        }
    }

    override fun onCleared() {        // главное — не забыть очиститься
        scope.cancel()
        player.release()
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

    override fun skipToNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
            player.play()
        }
    }

    override fun skipToPrevious() {
        when {
            player.currentPosition > RESTART_THRESHOLD_MS -> {
                player.seekTo(0)
            }
            player.hasPreviousMediaItem() -> {
                player.seekToPreviousMediaItem()
            }
            else -> {
                player.seekTo(0)
            }
        }
        player.play()
    }
}
