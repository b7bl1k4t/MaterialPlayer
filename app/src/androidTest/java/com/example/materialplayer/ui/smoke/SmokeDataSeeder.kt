package com.example.materialplayer.ui.smoke

import com.example.materialplayer.data.local.dao.AlbumDao
import com.example.materialplayer.data.local.dao.ArtistDao
import com.example.materialplayer.data.local.dao.TrackDao
import com.example.materialplayer.data.local.entity.AlbumEntity
import com.example.materialplayer.data.local.entity.ArtistEntity
import com.example.materialplayer.data.local.entity.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmokeDataSeeder @Inject constructor(
    private val artistDao: ArtistDao,
    private val albumDao: AlbumDao,
    private val trackDao: TrackDao
) {

    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (trackDao.countTracks() > 0) return@withContext

        /* ----- артисты ----- */
        val adeleId = upsertArtist("Adele")
        val toolId = upsertArtist("Tool")

        /* ----- альбомы ----- */
        val adeleAlbumId = upsertAlbum("21", adeleId)
        val toolAlbumId = upsertAlbum("Lateralus", toolId)

        /* ----- треки ----- */
        val fakeTracks = listOf(
            TrackEntity(
                id = 0,      // autoGenerate = true в schema? иначе задайте руками
                filePath = "/tmp/Adele/RollingInTheDeep.mp3",
                parentDir = "/tmp/Adele",
                playCount = 0,
                title = "Rolling In The Deep",
                artistId = adeleId,
                albumId = adeleAlbumId,
                artistName = "Adele",
                albumName = "Album",
                genre = "Pop",
                trackNo = 1,
                durationMs = 300
            ),
            TrackEntity(
                id = 0,
                filePath = "/tmp/Tool/Schism.flac",
                parentDir = "/tmp/Tool",
                playCount = 0,
                title = "Schism",
                artistId = toolId,
                albumId = toolAlbumId,
                artistName = "Tool",
                albumName = "Lateralus",
                genre = "Prog-Metal",
                trackNo = 2,
                durationMs = 250
            )
        )
        trackDao.insertTracks(fakeTracks)
    }

    /* -------------------------------------------------- */
    private suspend fun upsertArtist(name: String): Long {
        val inserted = artistDao.insertIgnore(ArtistEntity(0, name))
        return if (inserted != -1L) inserted
        else artistDao.findIdByName(name)!!
    }

    private suspend fun upsertAlbum(title: String, artistId: Long): Long {
        val inserted = albumDao.insertIgnore(
            AlbumEntity(0, title, artistId, null)
        )
        if (inserted != -1L) return inserted
        return albumDao.findIdByTitle(title)
            ?: throw IllegalStateException("Не удалось найти альбом с title='$title' после insertIgnore")
    }
}