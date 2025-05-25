package com.example.materialplayer.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.materialplayer.R
import com.example.materialplayer.domain.model.FolderItem

@Composable
fun ItemsList(
    items: List<FolderItem>,
    onFolder: (FolderItem) -> Unit,
    onTrack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = items,
            key = { it.path }
        ) { item ->
            ListItem(
                leadingContent = {
                    val icon = if (item.isFolder) R.drawable.ic_folder
                    else R.drawable.baseline_music_note_24
                    Icon(
                        painterResource(icon),
                        contentDescription = null
                    )
                },
                headlineContent = {
                    if (item.isFolder) {
                        Text(item.name ?: "Без названия")
                    } else {
                        Text(item.path.substringAfterLast('/'))
                    }
                },

                supportingContent = {
                    if (item.isFolder) {
                        Text("${item.subfolderCount} подпапок · ${item.trackCount} треков")
                    }
                },
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .clickable {
                        if (item.isFolder) onFolder(item)
                    }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
    }
}