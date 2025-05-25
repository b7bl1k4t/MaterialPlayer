package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.domain.model.*
import com.example.materialplayer.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    repo: LibraryRepository
) : ViewModel() {

    /* ─ текст запроса ─ */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query
    fun update(text: String) { _query.value = text }

    /* ─ результаты ─ */
    val results: StateFlow<List<SearchItem>> = combine(
        repo.allTracksByTitle(),        // Flow<List<Track>>
        repo.albumsWithArtist(),     // Flow<List<AlbumSummary>>
        repo.allArtists(),           // Flow<List<Artist>>
        _query
    ) { tracks, albums, artists, q ->
        if (q.isBlank()) emptyList() else {
            val ql = q.lowercase()

            /* треки */
            val t = tracks
                .filter { (it.title ?: it.filePath.substringAfterLast('/')).lowercase().contains(ql) }
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
}

/*  элементы для UI  */
sealed interface SearchItem {
    data class Track(val track: com.example.materialplayer.domain.model.Track, val artist: String)  : SearchItem
    data class Album(val album: AlbumSummary, val artist: String) : SearchItem
    data class Artist(val artist: com.example.materialplayer.domain.model.Artist) : SearchItem
}
