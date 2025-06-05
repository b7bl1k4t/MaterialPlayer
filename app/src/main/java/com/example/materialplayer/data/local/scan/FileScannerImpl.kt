package com.example.materialplayer.data.local.scan

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.example.materialplayer.data.local.entity.AlbumEntity
import com.example.materialplayer.data.local.entity.ArtistEntity
import com.example.materialplayer.data.local.entity.TrackEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Простой сканер аудиофайлов через SAF, без Flow и без сохранения обложек.
 */
class FileScannerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileScanner {
    private val audioExtensions: Set<String> = setOf("mp3", "flac", "wav", "m4a")

    /**
     * Сканирует указанные корневые директории (URI дерева) и возвращает список TrackEntity
     */
    override suspend fun scanRoots(roots: List<Uri>): List<ScanResult> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<ScanResult>()
            roots.forEach { rootUri ->
                DocumentFile.fromTreeUri(context, rootUri)
                    ?.walkSaf()
                    ?.filter { it.isFile }
                    ?.forEach { doc ->
                        val name = doc.name ?: return@forEach
                        val ext = name.substringAfterLast('.', "").lowercase()
                        if (ext !in audioExtensions) return@forEach

                        context.contentResolver.openFileDescriptor(doc.uri, "r")?.use { pfd ->
                            val mmr = MediaMetadataRetriever().apply {
                                setDataSource(pfd.fileDescriptor)
                            }
                            // Извлечение тегов
                            val title = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_TITLE
                            ) ?: name.substringBeforeLast('.')
                            val artistName = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_ARTIST
                            )
                            val albumName = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_ALBUM
                            )
                            val durationMs = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_DURATION
                            )?.toLongOrNull() ?: 0L
                            val genre = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_GENRE
                            )
                            val trackNumber = mmr.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER
                            )?.toIntOrNull()
                            mmr.release()

                            // Формируем сущности
                            val artistEntity = artistName
                                ?.takeIf { it.isNotBlank() }
                                ?.let { ArtistEntity(id = 0L, name = it.trim()) }

                            val albumEntity = albumName
                                ?.takeIf { it.isNotBlank() }
                                ?.let { AlbumEntity(
                                    id = 0L,
                                    title = it.trim(),
                                    artistId = null,
                                    coverUri = null
                                ) }


                            val parentDir = doc.parentFile?.uri?.toString()
                                ?: error("No parent for ${doc.uri}")
                            Log.d("SCAN_DEBUG", "save  parentDir = $parentDir")

                            val trackEntity = TrackEntity(
                                id = 0L,
                                filePath = doc.uri,
                                parentDir = parentDir,
                                durationMs = durationMs,
                                playCount = 0,
                                artistId = null,
                                albumId = null,
                                title = title,
                                artistName = artistName,
                                albumName = albumName,
                                genre = genre,
                                trackNumber = trackNumber
                            )

                            results += ScanResult(
                                track = trackEntity,
                                artist = artistEntity,
                                album = albumEntity
                            )
                        }
                    }
            }
            results
        }

    /**
     * Рекурсивный обход SAF-дерева через DocumentFile
     */
    private fun DocumentFile.walkSaf(): Sequence<DocumentFile> = sequence {
        yield(this@walkSaf)
        if (isDirectory) {
            listFiles().forEach { child ->
                yieldAll(child.walkSaf())
            }
        }
    }
}
