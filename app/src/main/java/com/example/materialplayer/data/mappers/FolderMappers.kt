package com.example.materialplayer.data.mappers

import com.example.materialplayer.data.local.dto.FolderItem
import com.example.materialplayer.domain.model.Folder

fun FolderItem.toDomain() = Folder(
    path,
    name,
    trackCount
)
