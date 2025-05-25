package com.example.materialplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.materialplayer.data.player.ExoPlaybackRepository
import com.example.materialplayer.data.player.PlaybackConnection
import com.example.materialplayer.data.player.PlaybackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {
    @Provides @Singleton
    fun provideExoPlayer(@ApplicationContext ctx: Context) =
        ExoPlayer.Builder(ctx).build()

    @Provides @Singleton
    fun providePlaybackRepo(
        player: ExoPlayer,
        ctx: Context
    ): PlaybackRepository = ExoPlaybackRepository(player, ctx)

    /** Connection, чтобы ViewModel совсем не трогали репозиторий напрямую */
    @Provides @Singleton
    fun providePlaybackConnection(repo: PlaybackRepository) = PlaybackConnection(repo)
}
