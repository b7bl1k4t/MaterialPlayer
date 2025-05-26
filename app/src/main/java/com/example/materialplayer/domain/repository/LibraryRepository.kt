package com.example.materialplayer.domain.repository

import android.net.Uri
import com.example.materialplayer.domain.model.AlbumDetail
import com.example.materialplayer.domain.model.AlbumSummary
import com.example.materialplayer.domain.model.Artist
import com.example.materialplayer.domain.model.ArtistDetail
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.FolderItem
import com.example.materialplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun allTracksByTitle(): Flow<List<Track>>
    fun childrenOf(path: String): Flow<List<FolderItem>>
    fun rootFolderItems(rootList: List<String>): Flow<List<BrowserItem>> // Новая функция для корней
    fun albumsWithArtist(): Flow<List<Pair<AlbumSummary, String>>>
    fun allArtists(): Flow<List<Artist>>                   // Artist list

    fun albumDetail(id: Long):  Flow<AlbumDetail>
    fun artistDetail(id: Long): Flow<ArtistDetail>

    fun folderFlow(basePath: String): Flow<List<BrowserItem>>
    fun mostPlayed(limit: Int = 100): Flow<List<Track>>
    suspend fun scanLibrary(roots: List<Uri>)
    suspend fun clearLibrary()
    suspend fun incrementPlayCount(trackId: Long)
}