package com.example.materialplayer.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.materialplayer.ui.composables.ItemsList
import com.example.materialplayer.ui.composables.LibraryPlaceholder
import com.example.materialplayer.ui.composables.ScrollableHeadline
import com.example.materialplayer.ui.viewmodel.LibraryViewModel

/* ------------ PUBLIC API ------------ */
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),       // :contentReference[oaicite:0]{index=0}
    onNavigateToSettings: () -> Unit
) {
    // React to data from the ViewModel
    val roots by viewModel.rootsFlow.collectAsState()
    val items by viewModel.itemsFlow.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()

    LaunchedEffect(items) {
        Log.d("LibraryScreen", "Current items count=${items.size}, items=$items")
    }

    // Enable system Back press only when we are inside a folder
    BackHandler(enabled = currentPath != null) {
        viewModel.onBack()
    }

    // Simple list without any sorting widgets for now

    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            roots.isEmpty() && currentPath == null -> LibraryPlaceholder(onNavigateToSettings)
            else -> {
                Column {
                    // ─── ЗАГОЛОВОК ────────────────────────────────
                    val title = currentPath
                        ?.let { Uri.decode(it).substringAfterLast('/') }
                        ?: "Roots"

                    ScrollableHeadline(title)
                    ItemsList(
                        items = items,
                        onFolder = { viewModel.onFolderClick(it.path) },
                        onTrack = { /* TODO: play track */ },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}