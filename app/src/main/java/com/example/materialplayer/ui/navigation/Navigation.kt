package com.example.materialplayer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(val route: String, val icon: ImageVector, val label: String) {
    object Library    : Navigation("library", Icons.Default.Home, "Library")
//    object NowPlaying    : Navigation("library", Icons.Default.Home, "Library")
    object Playlists  : Navigation("playlists", Icons.AutoMirrored.Filled.List, "Playlists")
    object Search     : Navigation("search", Icons.Default.Search,    "Search")
    object Settings   : Navigation("settings", Icons.Default.Settings,  "Settings")
}