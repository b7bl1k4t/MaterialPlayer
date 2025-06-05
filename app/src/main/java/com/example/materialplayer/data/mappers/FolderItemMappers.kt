package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.FolderItemDto
import com.example.materialplayer.domain.model.BrowserItem

fun FolderItemDto.toBrowserItem() = BrowserItem.Folder(
    path = path,
    name = path.substringAfterLast('/').substringAfterLast(':'),
    subfolderCount = subfolderCount,
    trackCount = trackCount
)
