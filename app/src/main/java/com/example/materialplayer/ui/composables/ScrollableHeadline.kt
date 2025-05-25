package com.example.materialplayer.ui.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableHeadline(title: String) {
    val scrollState = rememberScrollState()

    Text(
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Companion.Visible,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(16.dp),
        style = MaterialTheme.typography.headlineLarge
    )
}