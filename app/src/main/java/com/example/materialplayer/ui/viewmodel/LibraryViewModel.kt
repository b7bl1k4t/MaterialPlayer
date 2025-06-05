package com.example.materialplayer.ui.viewmodel

import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.preferences.RootsPreferences
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.ui.composables.LibraryMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri


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
                repo.folderFlow(path)    // Внутри папки
                    .onEach { Log.d("LibraryVM", "childrenOf($path) → $it") }
            }
        }
            .stateIn(viewModelScope, Eagerly, emptyList())


    /* Данные для остальных режимов */


    /** Назад по стеку (возвращает true если уходим из экрана) */
    // Обработка системной кнопки «назад»
    fun onBack(): Boolean {
        val cur = _current.value ?: return false
        Log.d("NAV", "cur=$cur  parent=${parentOf(cur)}  boundary=$rootBoundary")
        val parent = parentOf(cur)
        _current.value = parent
        return true
    }

    private var rootBoundary: String? = null

    fun resetToRoots() {
        rootBoundary = null
        _current.value = null
    }

    // Переход в папку
    fun onFolderClick(pathFromDb: String) {          // path уже encoded
        if (_current.value == null) {                   // кликнули root
            rootBoundary = DocumentsContract
                .getDocumentId(pathFromDb.toUri())         // docId!
        }
        _current.value = pathFromDb
    }

    /** Вызывается при клике на трек: инкремент + запуск воспроизведения */
    fun onTrackClick(track: Track) {
        viewModelScope.launch {
            repo.incrementPlayCount(track.id)
        }
    }

    // Возвращает родительский путь или null (если надо уйти в список корней)
    private fun parentOf(curEnc: String): String? {
        val uri = curEnc.toUri()
        val docId = DocumentsContract.getDocumentId(uri)
        val cut = docId.lastIndexOf('/')
        if (cut < 0) return null                        // уже root
        val parentId = docId.substring(0, cut)

        val parentUri = DocumentsContract.buildDocumentUriUsingTree(
            uri, parentId).toString()

        return parentUri.takeIf {
            rootBoundary?.let(parentId::startsWith) ?: true
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Uri>>.asRootItems(): Flow<List<BrowserItem.Folder>> =
        mapLatest { roots -> roots.map { repo.rootItem(it) } }
}

