package com.example.materialplayer.di

import com.example.materialplayer.data.repository.RoomHistoryRepository
import com.example.materialplayer.data.repository.RoomLibraryRepository
import com.example.materialplayer.data.repository.RoomPlaylistRepository
import com.example.materialplayer.domain.repository.HistoryRepository
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.domain.repository.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindLibraryRepo(impl: RoomLibraryRepository): LibraryRepository

    @Binds @Singleton
    abstract fun bindHistoryRepo(impl: RoomHistoryRepository): HistoryRepository

    @Binds @Singleton
    abstract fun bindPlaylistRepo(impl: RoomPlaylistRepository): PlaylistRepository
}