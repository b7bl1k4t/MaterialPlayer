package com.example.materialplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.materialplayer.data.player.PlaybackConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaybackHolder @Inject constructor(
    val connection: PlaybackConnection // просто прокидываем наружу
) : ViewModel()
