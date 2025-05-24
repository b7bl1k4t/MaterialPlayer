package com.example.materialplayer.domain.model

data class Folder(
    val path: String,
    val name: String,
    val parentDir: String,
    val subfolderCount: Int,
    val trackCount: Int
)
