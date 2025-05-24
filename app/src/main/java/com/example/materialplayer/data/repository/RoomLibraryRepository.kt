package com.example.materialplayer.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.materialplayer.data.local.dao.*
import com.example.materialplayer.data.local.entity.*
import com.example.materialplayer.data.local.scan.FileScannerImpl
import com.example.materialplayer.data.local.scan.ScanResult
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.domain.model.AlbumDetail
import com.example.materialplayer.domain.model.ArtistDetail
import com.example.materialplayer.domain.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class RoomLibraryRepository @Inject constructor(
    private val trackDao: TrackDao,
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao,
    private val scanner: FileScannerImpl
) : LibraryRepository {

    override suspend fun rescanLibrary(roots: List<Uri>) = withContext(Dispatchers.IO) {
        // 1) сканируем аудио и получаем ScanResult (track + artist? + album?)
        val results: List<ScanResult> = scanner.scanRoots(roots)

        // 2) очищаем все таблицы
        trackDao.deleteAll()
        artistDao.deleteAll()
        albumDao.deleteAll()

        // 3) собираем и сохраняем артистов
        val artistNames = results.mapNotNull { it.artist?.name }.distinct()
        val artistEntities = artistNames.map { name -> ArtistEntity(id = 0L, name = name) }
        artistDao.insertAll(artistEntities)
        // создаём карту name → generatedId
        val artistMap = artistNames.associateWith { name ->
            artistDao.findByName(name)!!.id
        }

        // 4) собираем и сохраняем альбомы
        val albumKeys = results.mapNotNull { res ->
            val alb = res.album
            val artName = res.artist?.name
            if (alb != null && artName != null) alb.title to artName else null
        }.distinct()
        val albumEntities = albumKeys.map { (title, artName) ->
            // проставляем artistId из карты
            AlbumEntity(
                id = 0L,
                title = title,
                artistId = artistMap[artName]!!,
                coverUri = null
            )
        }
        albumDao.insertAll(albumEntities)
        // карта (title, artistId) → generatedId
        val albumMap = albumKeys.associate { (title, artName) ->
            val artId = artistMap[artName]!!
            (title to artId) to albumDao
                .findByTitleAndArtist(title, artId)!!
                .id
        }

        // 5) обогащаем треки FK и сохраняем их
        val trackEntities: List<TrackEntity> = results.map { res ->
            val artId = res.artist?.name?.let { artistMap[it] }
            val albId = res.album?.title?.let { title ->
                val artName = res.artist!!.name
                albumMap[title to artistMap[artName]!!]
            }
            res.track.copy(artistId = artId, albumId = albId)
        }
        trackDao.insertAll(trackEntities)
    }

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

    override fun getFolderInfos(basePath: String) =
        trackDao.getFolderInfos()
            .map { list -> list.filter { it.parentDir == basePath } .map { it.toDomain() } }

    override fun mostPlayed(limit: Int) =
        trackDao.mostPlayed(limit).map { list -> list.map(TrackEntity::toDomain) }

    fun getArtistDetail(id: Long): Flow<ArtistDetail> =
        artistDao.getArtistWithAlbums(id).map { it.toDomain() }

    fun getAlbumDetail(id: Long): Flow<AlbumDetail> =
        albumDao.getAlbumWithTracks(id).map { it.toDomain() }
}
