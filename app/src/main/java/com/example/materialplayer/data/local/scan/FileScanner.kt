package com.example.materialplayer.data.local.scan

import android.net.Uri

/**
 * Интерфейс сканнера аудиофайлов через SAF,
 * возвращающего сущности трека, артиста и альбома без работы с БД.
 */
interface FileScanner {
    /**
     * Рескан корневых Uri: эмитит ScanResult по мере обхода.
     * Артист или альбом может быть null, если соответствующие теги отсутствуют.
     */
    suspend fun scanRoots(roots: List<Uri>): List<ScanResult>
}