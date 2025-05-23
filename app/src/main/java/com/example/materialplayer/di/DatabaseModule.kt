package com.example.materialplayer.di

import android.content.Context
import androidx.room.Room
import com.example.materialplayer.data.local.db.MaterialPlayerRoomDatabase
import com.example.materialplayer.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): MaterialPlayerRoomDatabase =
        Room.databaseBuilder(
            ctx,
            MaterialPlayerRoomDatabase::class.java,
            "player.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideArtistDao(db: MaterialPlayerRoomDatabase): ArtistDao =
        db.artistDao()

    @Provides
    fun provideAlbumDao(db: MaterialPlayerRoomDatabase): AlbumDao =
        db.albumDao()

    @Provides
    fun provideTrackDao(db: MaterialPlayerRoomDatabase): TrackDao =
        db.trackDao()

    @Provides
    fun providePlaybackHistoryDao(db: MaterialPlayerRoomDatabase): PlaybackHistoryDao =
        db.playbackHistoryDao()

    @Provides
    fun providePlaylistDao(db: MaterialPlayerRoomDatabase): PlaylistDao =
        db.playlistDao()
}
