package com.example.materialplayer.ui.smoke

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun TitleListScreen(vm: TitleListViewModel = hiltViewModel()) {
    val lazyItems = vm.tracksByTitle().collectAsLazyPagingItems()

    // Горизонтальный список треков
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    ) {
        items(
            count = lazyItems.itemCount,
            key = lazyItems.itemKey { it.id }
        ) { index ->
            val track = lazyItems[index] ?: return@items
            Card(
                Modifier
                    .wrapContentWidth()
                    .clickable { vm.onTrackClicked(track.id) }
            ) {
                Text(
                    text = "${track.artistName ?: "?"} - ${track.title}",
                    Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        when (lazyItems.loadState.append) {
            is LoadState.Loading -> item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {}
        }
    }
}