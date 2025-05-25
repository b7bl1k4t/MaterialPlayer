package com.example.materialplayer.ui.screens

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun NowPlayingScreen(
    exoPlayer: ExoPlayer,
    onBack: () -> Unit = {}                // навигатор передаст NavController::popBackStack
) {
    // Чтобы PlayerView не пересоздавался при каждой перестройке
    val ctx = LocalContext.current
    val playerView = remember {
        PlayerView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            useController = true
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT  // ← так правильно
            player = exoPlayer
        }
    }

    // Обработаем back
    BackHandler { onBack() }

    // Когда Composable покидает Composition — выключаем полноэкранный UI, но
    // сами ExoPlayer не релизим (мини-плеер продолжит работать)
    DisposableEffect(Unit) {
        onDispose {
            playerView.player?.playWhenReady = false   // ставим на паузу
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {

            // Сам видео/арт-область; PlayerView умеет показывать как аудио-обложку,
            // так и видео, если файл видео.
            AndroidView(
                factory  = { playerView },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)            // займёт всё свободное пространство
            )

            // Дополнительная панель (трек, позиция) — опционально
            PositionBar(exoPlayer, Modifier.fillMaxWidth())
        }
    }
}

/* ---------- простая текстовая позиция + прогресс ---------- */
@Composable
private fun PositionBar(
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    var pos by remember { mutableStateOf(0L) }
    var dur by remember { mutableStateOf(0L) }

    // слушаем изменения позиции раз в 500 мс
    LaunchedEffect(player) {
        while (true) {
            pos = player.currentPosition
            dur = player.duration.takeIf { it > 0 } ?: 0
            kotlinx.coroutines.delay(500)
        }
    }

    Column(modifier.padding(12.dp)) {
        androidx.compose.material3.Text(
            text = "${toTime(pos)} / ${toTime(dur)}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(4.dp))
        androidx.compose.material3.LinearProgressIndicator(
            progress = if (dur > 0) pos / dur.toFloat() else 0f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun toTime(ms: Long): String {
    val totalSec = ms / 1000
    val m = (totalSec / 60).toInt()
    val s = (totalSec % 60).toInt()
    return "%02d:%02d".format(m, s)
}
