package com.example.materialplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.materialplayer.data.local.dao.*
import com.example.materialplayer.data.local.dto.FolderItem
import com.example.materialplayer.data.local.entity.*
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.domain.model.AlbumDetail
import com.example.materialplayer.domain.model.ArtistDetail
import com.example.materialplayer.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomLibraryRepository @Inject constructor(
    private val trackDao: TrackDao,
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao
) : LibraryRepository {

    override fun tracksByTitle() =
        Pager(PagingConfig(60)) { trackDao.tracksByTitle() }
            .flow.map { it.map(TrackEntity::toDomain) }

    override fun tracksByFolder(basePath: String) =
        Pager(PagingConfig(60)) { trackDao.tracksByPath() }
            .flow.map { it.map(TrackEntity::toDomain) }

    override fun tracksByArtist(artistId: Long) =
        Pager(PagingConfig(60)) { trackDao.tracksByArtist(artistId) }
            .flow.map { it.map(TrackEntity::toDomain) }

    override fun tracksByAlbum(albumId: Long) =
        Pager(PagingConfig(60)) { trackDao.tracksByAlbum(albumId) }
            .flow.map { it.map(TrackEntity::toDomain) }

    override fun subfolders(basePath: String) =
        trackDao.subfolders(basePath).map { list -> list.map(FolderItem::toDomain) }

    override fun mostPlayed(limit: Int) =
        trackDao.mostPlayed(limit).map { list -> list.map(TrackEntity::toDomain) }

    fun getArtistDetail(id: Long): Flow<ArtistDetail> =
        artistDao.getArtistWithAlbums(id).map { it.toDomain() }

    fun getAlbumDetail(id: Long): Flow<AlbumDetail> =
        albumDao.getAlbumWithTracks(id).map { it.toDomain() }
}
