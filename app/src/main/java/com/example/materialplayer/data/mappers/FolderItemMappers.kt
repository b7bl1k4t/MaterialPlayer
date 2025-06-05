package com.example.materialplayer.data.mappers

import android.net.Uri
import android.provider.DocumentsContract
import com.example.materialplayer.data.local.dto.FolderItemDto
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.util.safeDocId
import androidx.core.net.toUri

fun FolderItemDto.toBrowserItem() = BrowserItem.Folder(
    path = path,                                 // **не трогаем**
    name = path.toUri().lastPathSegment
        ?.substringAfterLast(':') ?: "—",
    subfolderCount = subfolderCount,
    trackCount = trackCount
)
