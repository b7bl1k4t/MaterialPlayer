package com.example.materialplayer.data.local.dto

data class FolderItem(
    val path: String,
    val name: String,
    val parentDir: String,
    val subfolderCount: Int,
    val trackCount: Int
)
