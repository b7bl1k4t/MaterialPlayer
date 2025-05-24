package com.example.materialplayer.di

import com.example.materialplayer.data.local.scan.FileScannerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanModule {
    @Provides @Singleton
    fun provideFileScanner(impl: FileScannerImpl): FileScannerImpl = impl
}
