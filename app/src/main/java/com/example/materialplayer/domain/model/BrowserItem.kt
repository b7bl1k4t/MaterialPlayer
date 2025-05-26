package com.example.materialplayer.domain.model

sealed interface BrowserItem {
    val path: String
    val name: String

    data class Folder(
        override val path: String,
        override val name: String,
        val subfolderCount: Int,
        val trackCount: Int
    ) : BrowserItem

    data class TrackEntry(
        override val path: String,
        override val name: String,
        val track: Track                    // ← полноценный Track
    ) : BrowserItem
}
