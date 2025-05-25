package com.example.materialplayer.di

import com.example.materialplayer.data.local.scan.FileScanner
import com.example.materialplayer.data.local.scan.FileScannerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScanModule {
    @Binds
    @Singleton
    abstract fun bindFileScanner(impl: FileScannerImpl): FileScanner
}
