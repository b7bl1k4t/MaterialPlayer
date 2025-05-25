package com.example.materialplayer.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.preferences.RootsPreferences
import com.example.materialplayer.domain.model.FolderItem
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.ui.composables.LibraryMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import javax.inject.Inject


@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repo: LibraryRepository,
    rootsPrefs: RootsPreferences,
) : ViewModel() {

    /* текущий режим */
    private val _mode = MutableStateFlow(LibraryMode.Folder)
    val mode: StateFlow<LibraryMode> = _mode
    fun setMode(mode: LibraryMode) { _mode.value = mode }

    val titles = repo.allTracksByTitle()
    val albums = repo.albumsWithArtist()
    val artists = repo.allArtists()

    init {
        // ВСЕГДА слушаем rootsPrefs и сбрасываем, если он стал пустым
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
    .onEach { roots ->
        Log.d("LibraryVM", "rootsPrefs → $roots")
    }
    .stateIn(viewModelScope, Eagerly, emptyList())


    // Текущий выбранный путь (null = показываем корни)
    private val _current = MutableStateFlow<String?>(null)
        .apply {
            onEach { Log.d("LibraryVM", "currentPath → $it") }
        }

    val currentPath: MutableStateFlow<String?> = _current

    /**
     * Для списка корней: берём каждый URI, конвертим его в doc-base
     * и через DAO считаем реальные subfolderCount/trackCount.
     */

    @OptIn(ExperimentalCoroutinesApi::class)
    val itemsFlow: StateFlow<List<FolderItem>> =
        _current.flatMapLatest { path ->
            if (path == null) {
                rootsFlow.asRootItems()
            } else {
                repo.folderFlow(path)
                    .onEach { Log.d("LibraryVM", "childrenOf($path) → $it") }
            }
        }
            .stateIn(viewModelScope, Eagerly, emptyList())


    /**
     * Обработка системной «назад».
     * Возвращаем true, если «перехватили» (т.е. внутри папки).
     */
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

    fun onFolderClick(path: String) {
        if (rootBoundary == null) rootBoundary = path   // запомним root один раз
        _current.value = path
    }

    /** Возвращает родительский путь или null (если надо уйти в список корней). */
    private fun computeParent(cur: String): String? {
        val root = rootBoundary ?: return null
        if (!cur.startsWith(root)) return null

        // относительный остаток без префикса root
        val rel = cur.removePrefix(root).trimStart('/')
        if (rel.isEmpty()) {
            // мы прямо в root — следующая «назад» уходит в корни
            return null
        }

        val segments = rel.split('/')
        return if (segments.size == 1) {
            // непосредственный потомок root — возвращаемся в root
            root
        } else {
            // более глубокая вложенность — отсекаем последний сегмент
            root + "/" + segments.dropLast(1).joinToString("/")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<Uri>>.asRootItems(): Flow<List<FolderItem>> =
        flatMapLatest { roots ->
            combine(roots.map { uri ->
                val tree = Uri.decode(uri.toString())
                val docBase = if ("/document/" in tree) tree
                else {
                    val id = tree.substringAfter("/tree/")
                    "$tree/document/$id"
                }

                // repo.childrenOf возвращает уже и подпапки, и треки,
                // но нам нужен итоговый count:
                repo.folderFlow(docBase)
                    .map { list ->
                        val folders = list.filter { it.isFolder }
                        val tracks  = list.filter { !it.isFolder }
                        FolderItem(
                            path = tree,
                            name = uri.lastPathSegment?.substringAfterLast(':') ?: "root",
                            parentPath = "",
                            isFolder = true,
                            subfolderCount = folders.size,
                            trackCount = folders.sumOf { it.trackCount } + tracks.size
                        )
                    }
            }) { items -> items.toList() }
        }
}

