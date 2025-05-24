package com.example.materialplayer.data.local.scan

import com.example.materialplayer.data.local.entity.ArtistEntity
import com.example.materialplayer.data.local.entity.AlbumEntity
import com.example.materialplayer.data.local.entity.TrackEntity

/**
 * Результат сканирования одного аудиофайла:
 * сущности трека, артиста и альбома (если доступны)
 */
data class ScanResult(
    val track: TrackEntity,
    val artist: ArtistEntity?,
    val album: AlbumEntity?
)