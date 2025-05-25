package com.example.materialplayer.player

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.materialplayer.data.player.ExoPlaybackRepository

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject lateinit var playbackRepo: ExoPlaybackRepository
    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSession.Builder(this, playbackRepo.player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }
}
