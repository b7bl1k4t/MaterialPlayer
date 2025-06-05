package com.example.materialplayer.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.preferences.RootsPreferences
import com.example.materialplayer.domain.model.BrowserItem
import com.example.materialplayer.domain.model.Track
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.ui.composables.LibraryMode
import com.example.materialplayer.util.displayName
import com.example.materialplayer.util.docBaseEncoded
import com.example.materialplayer.util.parentEncoded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.flatten
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
                repo.folderFlow(Uri.decode(path))    // Внутри папки
                    .onEach { Log.d("LibraryVM", "childrenOf($path) → $it") }
            }
        }
            .stateIn(viewModelScope, Eagerly, emptyList())


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
    fun onFolderClick(rawPath: String) {
        val enc = rawPath                           // уже encoded
        if (rootBoundary == null) rootBoundary = enc
        _current.value = enc
    }

    /** Вызывается при клике на трек: инкремент + запуск воспроизведения */
    fun onTrackClick(track: Track) {
        viewModelScope.launch {
            repo.incrementPlayCount(track.id)
        }
    }

    // Возвращает родительский путь или null (если надо уйти в список корней)
    private fun computeParent(curEnc: String): String? =
        curEnc.toUri().parentEncoded()
            ?.toString()
            ?.takeIf { rootBoundary == null || it.startsWith(rootBoundary!!) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Uri>>.asRootItems(): Flow<List<BrowserItem.Folder>> =
        mapLatest { roots -> roots.map { repo.rootItemFor(it) } }
}

