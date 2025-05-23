package com.example.materialplayer.domain.repository

import androidx.paging.PagingData
import com.example.materialplayer.domain.model.Folder
import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun tracksByTitle(): Flow<PagingData<Track>>
    fun tracksByFolder(basePath: String): Flow<PagingData<Track>>
    fun tracksByArtist(artistId: Long): Flow<PagingData<Track>>
    fun tracksByAlbum(albumId: Long): Flow<PagingData<Track>>
    fun subfolders(basePath: String): Flow<List<Folder>>
    fun mostPlayed(limit: Int = 100): Flow<List<Track>>
}