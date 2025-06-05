package com.example.materialplayer.data.repository

import android.net.Uri
import android.util.Log
import com.example.materialplayer.data.local.dao.*
import com.example.materialplayer.data.local.dto.FolderItemDto
import com.example.materialplayer.data.local.entity.*
import com.example.materialplayer.data.local.scan.FileScannerImpl
import com.example.materialplayer.data.local.scan.ScanResult
import com.example.materialplayer.data.mappers.toBrowserItem
import com.example.materialplayer.data.mappers.toDomain
import com.example.materialplayer.data.mappers.toSummary
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.util.displayName
import com.example.materialplayer.util.docBaseDecoded
import com.example.materialplayer.util.docBaseEncoded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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

    companion object {
        private const val UNKNOWN = "Unknown"
    }

    override suspend fun clearLibrary() {
        // удаляем всё из таблиц – UI-поток, подписанный на DAO, сразу получит пустые списки
        trackDao.deleteAll()
        albumDao.deleteAll()
        artistDao.deleteAll()
    }

    override suspend fun incrementPlayCount(trackId: Long) {
        trackDao.incrementPlayCount(trackId)
    }

    override suspend fun scanLibrary(roots: List<Uri>) = withContext(Dispatchers.IO) {
        // 1) сканируем аудио и получаем ScanResult (track + artist? + album?)
        Log.d("SCAN", "start rescan on ${roots.size} roots")
        val results: List<ScanResult> = scanner.scanRoots(roots)
        Log.d("SCAN", "rescan finished")

        // 2) очищаем все таблицы
        clearLibrary()

        // 3) собираем и сохраняем артистов
        val artistNames = results
            .map { it.artist?.name?.takeIf { it.isNotBlank() } ?: UNKNOWN }
            .distinct()

        val artistEntities = artistNames.map { name -> ArtistEntity(id = 0L, name = name) }
        artistDao.insertAll(artistEntities)

        val artistMap: Map<String, Long> = artistNames.associateWith { name ->
            artistDao.findIdByName(name)
                ?: error("Не найден артист '$name' после insertAll()")
        }

        // 4) собираем и сохраняем альбомы
        val albumKeys = results
            .map { res ->
                res.run {
                    val title = album?.title ?: UNKNOWN
                    val artName = artist?.name ?: UNKNOWN
                    title to artName
                }
            }
            .distinct()

        val albumEntities = albumKeys.map { (title, artName) ->
            val artId = artistMap[artName] ?: error("Нет artistId для '$artName'")
            AlbumEntity(
                id = 0L,
                title = title,
                artistId = artId,
                coverUri = null
            )
        }
        albumDao.insertAll(albumEntities)

        val albumMap: Map<Pair<String, Long>, Long> = albumKeys.associate { (title, artName) ->
            val artId = artistMap[artName] ?: error("Нет artistId для '$artName'")
            val album = requireNotNull(albumDao.findByTitleAndArtist(title, artId)) {
                "После вставки не найден альбом '$title' для артиста '$artName'"
            }
            (title to artId) to album.id
        }

        // 5) обогащаем треки FK и сохраняем их
        val trackEntities = results.map { res ->
            val artName  = res.artist?.name ?: UNKNOWN
            val artId = artistMap[artName] ?: error("Нет artistId для '$artName'")
            val albTitle = res.album?.title ?: UNKNOWN
            val albId = albumMap[albTitle to artId] ?: error("Нет albumId для '$albTitle' и artId=$artId")

            res.track.copy(
                artistId   = artId,
                albumId    = albId,
                artistName = artName,
                albumName  = albTitle
            )
        }

        trackDao.insertAll(trackEntities)
    }

    override fun allTracksByTitle(): Flow<List<Track>> =
        trackDao.allTracksByTitle().map { list -> list.map { it.toDomain() } }

    override fun albumsWithArtist() =
        albumDao.albumsWithArtist().map { list -> list.map { it.toSummary() } }

    override fun allArtists() =
        artistDao.allArtists().map { list -> list.map { it.toDomain() } }

    override fun albumDetail(id: Long) =
        albumDao.getAlbumWithTracks(id).map { it.toDomain() }

    override fun artistDetail(id: Long) =
        artistDao.getArtistWithAlbums(id).map { it.toDomain() }

    override fun mostPlayed(limit: Int) =
        trackDao.mostPlayed(limit).map { list -> list.map(TrackEntity::toDomain) }

    override fun rootFolderItems(rootList: List<String>): Flow<List<BrowserItem>> {
        return flow {
            emit(
                rootList.map { path ->
                    BrowserItem.Folder(
                        path = path,
                        name = path.substringAfterLast('/'),
                        subfolderCount = 0, // Здесь нужно запросить количество подпапок
                        trackCount = 0 // Здесь нужно запросить количество треков
                    )
                }
            )
        }
    }

    override suspend fun rootItem(uri: Uri): BrowserItem.Folder {
        val base = uri.docBaseEncoded()

        // subfolderCount = сколько строк вернул getFolderInfos
        val subfolderCount =
            trackDao.getFolderInfos(base).first().size

        // trackCount  = сколько треков лежит прямо в каталоге
        val trackCount = trackDao.countTracksRecursive(base)

        return BrowserItem.Folder(
            path = base,
            name = uri.displayName,
            subfolderCount = subfolderCount,
            trackCount = trackCount
        )
    }


    override fun folderFlow(basePath: String): Flow<List<BrowserItem>> {
        val foldersFlow = trackDao.getFolderInfos(basePath)
            .map { it.map(FolderItemDto::toBrowserItem) }

        val tracksFlow = trackDao.getTracksInFolder(basePath)
            .map { it.map(TrackEntity::toBrowserItem) }


        return combine(foldersFlow, tracksFlow, List<BrowserItem>::plus)
    }
}