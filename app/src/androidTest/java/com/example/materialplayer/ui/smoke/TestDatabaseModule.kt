package com.example.materialplayer.ui.smoke

import android.content.Context
import androidx.room.Room
import com.example.materialplayer.data.local.dao.AlbumDao
import com.example.materialplayer.data.local.dao.ArtistDao
import com.example.materialplayer.data.local.dao.PlaybackHistoryDao
import com.example.materialplayer.data.local.dao.PlaylistDao
import com.example.materialplayer.data.local.dao.TrackDao
import com.example.materialplayer.data.local.db.MaterialPlayerRoomDatabase
import com.example.materialplayer.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]      // заменяем ваш продакшен-модуль
)
object TestDatabaseModule {
    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext ctx: Context): MaterialPlayerRoomDatabase =
        Room.inMemoryDatabaseBuilder(ctx, MaterialPlayerRoomDatabase::class.java)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides fun provideTrackDao(db: MaterialPlayerRoomDatabase): TrackDao =
        db.trackDao()

    @Provides fun provideArtistDao(db: MaterialPlayerRoomDatabase): ArtistDao =
        db.artistDao()

    @Provides fun provideAlbumDao(db: MaterialPlayerRoomDatabase): AlbumDao =
        db.albumDao()

    @Provides fun provideHistoryDao(db: MaterialPlayerRoomDatabase): PlaybackHistoryDao =
        db.playbackHistoryDao()

    @Provides fun providePlaylistDao(db: MaterialPlayerRoomDatabase): PlaylistDao =
        db.playlistDao()

    @Provides
    @Singleton
    fun provideSmokeDataSeeder(db: MaterialPlayerRoomDatabase): SmokeDataSeeder =
        SmokeDataSeeder(
            db.artistDao(),
            db.albumDao(),
            db.trackDao()
        )
}