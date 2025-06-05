package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.domain.model.*
import com.example.materialplayer.domain.repository.LibraryRepository
import com.example.materialplayer.util.displayName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: LibraryRepository
) : ViewModel() {

    /* ─ текст запроса ─ */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query
    fun update(text: String) { _query.value = text }

    /* ─ результаты ─ */
    val results: StateFlow<List<SearchItem>> = combine(
        repo.allTracksByTitle(), // Flow<List<Track>>
        repo.albumsWithArtist(), // Flow<List<AlbumSummary>>
        repo.allArtists(), // Flow<List<Artist>>
        _query
    ) { tracks, albums, artists, q ->
        if (q.isBlank()) emptyList() else {
            val ql = q.lowercase()

            /* треки */
            val t = tracks
                .filter { (it.title ?: it.filePath.displayName).lowercase().contains(ql) }
                .map { SearchItem.Track(it, it.artistName ?: "Unknown") }

            /* альбомы */
            val al = albums
                .filter { it.second.lowercase().contains(ql) }
                .map { SearchItem.Album(it.first, it.second) }

            /* исполнители */
            val ar = artists
                .filter { it.name.lowercase().contains(ql) }
                .map { SearchItem.Artist(it) }

            t + al + ar
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** Вызывается при клике на трек: инкремент + запуск воспроизведения */
    fun onTrackClick(track: Track) {
        viewModelScope.launch {
            repo.incrementPlayCount(track.id)
        }
    }
}

/*  элементы для UI  */
sealed interface SearchItem {
    data class Track(val track: com.example.materialplayer.domain.model.Track, val artist: String)  : SearchItem
    data class Album(val album: AlbumSummary, val artist: String) : SearchItem
    data class Artist(val artist: com.example.materialplayer.domain.model.Artist) : SearchItem
}
