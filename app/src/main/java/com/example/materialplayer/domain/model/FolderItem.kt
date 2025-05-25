package com.example.materialplayer.domain.model

data class FolderItem(
    val path: String,          // уникальный ключ (полный SAF-/DAO-путь)
    val name: String?,          // отображаемое имя (после последнего '/')
    val parentPath: String,    // полный путь родителя ("" для списка root’ов)
    val isFolder: Boolean,     // true — каталог, false — файл
    val trackCount: Int = 0,   // треки ПРЯМО в этой папке
    val subfolderCount: Int = 0// количество подпапок
)
