package com.example.materialplayer.data.mappers

import android.net.Uri
import com.example.materialplayer.data.local.dto.FolderItemDto
import com.example.materialplayer.data.local.entity.TrackEntity
import com.example.materialplayer.domain.model.FolderItem

fun FolderItemDto.toFolderItem(): FolderItem {
    val name = Uri.decode(path).substringAfterLast('/')
    val parent = Uri.decode(path).substringBeforeLast('/', "")
    return FolderItem(
        path = path,
        name = name,
        parentPath = parent,
        isFolder = true,
        trackCount = trackCount,
        subfolderCount = subfolderCount
    )
}

fun TrackEntity.toFolderItem(): FolderItem =
    FolderItem(
        path = Uri.decode(filePath),
        name = title,
        parentPath = Uri.decode(parentDir),
        isFolder = false,
        trackCount = 0,
        subfolderCount = 0
    )

