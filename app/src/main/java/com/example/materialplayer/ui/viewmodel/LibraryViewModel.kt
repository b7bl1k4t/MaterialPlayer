package com.example.materialplayer.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.preferences.RootsPreferences
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.ui.composables.LibraryMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.flatten


@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repo: LibraryRepository,
    private val rootsPrefs: RootsPreferences,
) : ViewModel() {

    /** Текущий режим (папки, по названию, альбомы, исполнители) */
    private val _mode = MutableStateFlow(LibraryMode.Folder)
    val mode: StateFlow<LibraryMode> = _mode
    fun setMode(mode: LibraryMode) { _mode.value = mode }

    val titles = repo.allTracksByTitle()
    val albums = repo.albumsWithArtist()
    val artists = repo.allArtists()

    // Всегда слушаем rootsPrefs и сбрасываем, если он стал пустым
    init {
        rootsPrefs.rootsFlow
            .onEach { roots ->
                if (roots.isEmpty()) {
                    resetToRoots()
                }
            }
            .launchIn(viewModelScope)
    }

    // Получаем из prefs Flow<List<Uri>> корней
    val rootsFlow = rootsPrefs.rootsFlow      // Flow<List<Uri>>
        .onEach { roots -> Log.d("LibraryVM", "rootsPrefs → $roots") }
        .stateIn(viewModelScope, Eagerly, emptyList())


    /** Текущий путь (null → список root-ов) */
    private val _current = MutableStateFlow<String?>(null)
        .apply { onEach { Log.d("LibraryVM", "currentPath → $it") } }
    val currentPath: MutableStateFlow<String?> = _current


    /** Список элементов для Folder-режима */
    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsFlow: StateFlow<List<BrowserItem>> =
        _current.flatMapLatest { path ->
            if (path == null) {
                rootsFlow.asRootItems()  // Отображаем корни
            } else {
                repo.folderFlow(path)  // Внутри папки
                    .onEach { Log.d("LibraryVM", "childrenOf($path) → $it") }
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    /* Данные для остальных режимов */


    /** Назад по стеку (возвращает true если уходим из экрана) */
    // Обработка системной кнопки «назад»
    fun onBack(): Boolean {
        val cur = _current.value ?: return false
        val parent = computeParent(cur)
        _current.value = parent
        return true
    }

    private var rootBoundary: String? = null

    fun resetToRoots() {
        rootBoundary = null
        _current.value = null
    }

    // Переход в папку
    fun onFolderClick(path: String) {
        if (rootBoundary == null) rootBoundary = path
        _current.value = path
    }

    // Возвращает родительский путь или null (если надо уйти в список корней)
    private fun computeParent(cur: String): String? {
        val root = rootBoundary ?: return null
        if (!cur.startsWith(root)) return null

        val rel = cur.removePrefix(root).trimStart('/')
        if (rel.isEmpty()) {
            return null
        }

        val segments = rel.split('/')
        return if (segments.size == 1) {
            root
        } else {
            root + "/" + segments.dropLast(1).joinToString("/")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Uri>>.asRootItems(): Flow<List<BrowserItem>> =
        flatMapLatest { roots ->
            // Для каждого URI создаём отдельный поток с элементами
            flow {
                val items = roots.map { uri ->
                    val tree = Uri.decode(uri.toString())
                    val docBase = if ("/document/" in tree) tree else {
                        val id = tree.substringAfter("/tree/")
                        "$tree/document/$id"
                    }

                    // Получаем элементы из папки для каждого пути
                    val list = repo.folderFlow(docBase).first()
                    val folders = list.filterIsInstance<BrowserItem.Folder>()
                    val tracks = list.filterIsInstance<BrowserItem.TrackEntry>()

                    val folderItem = BrowserItem.Folder(
                        path = tree,
                        name = uri.lastPathSegment?.substringAfterLast(':') ?: "root",
                        subfolderCount = folders.size,
                        trackCount = tracks.size
                    )

                    // Преобразуем треки в TrackEntry
                    val trackItems: List<BrowserItem.TrackEntry> = tracks.map { track ->
                        BrowserItem.TrackEntry(
                            path = track.path,
                            name = track.name,
                            track = track.track  // Преобразуем в TrackEntry
                        )
                    }

                    // Возвращаем сначала папки, потом треки
                    listOf(folderItem) + trackItems
                }

                // Мы можем безопасно вызвать flatten, так как items - это список List<BrowserItem>
                emit(items.flatten())
            }
        }
}

