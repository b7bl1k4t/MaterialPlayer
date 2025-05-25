package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repo: LibraryRepository,
    saved: SavedStateHandle
) : ViewModel() {
    private val id: Long = saved.get<Long>("id")!!
    val state = repo.artistDetail(id).stateIn(viewModelScope, SharingStarted.Lazily, null)
}