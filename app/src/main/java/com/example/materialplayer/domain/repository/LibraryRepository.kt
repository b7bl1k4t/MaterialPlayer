package com.example.materialplayer.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.example.materialplayer.domain.model.FolderItem
import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun tracksByTitle(): Flow<PagingData<Track>>
    fun tracksByArtist(artistId: Long): Flow<PagingData<Track>>
    fun tracksByAlbum(albumId: Long): Flow<PagingData<Track>>
    fun childrenOf(basePath: String): Flow<List<FolderItem>>
    fun mostPlayed(limit: Int = 100): Flow<List<Track>>
    suspend fun scanLibrary(roots: List<Uri>)
    suspend fun clearLibrary()
}