package com.example.materialplayer.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.materialplayer.ui.navigation.Navigation
import com.example.materialplayer.ui.viewmodel.AuthViewModel
import com.example.materialplayer.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsVm: SettingsViewModel = hiltViewModel(),
    authVm: AuthViewModel = hiltViewModel(),
    nav: NavController,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val ui by settingsVm.uiState.collectAsState()
    var toDelete by remember { mutableStateOf<Uri?>(null) }
    val user by authVm.currentUser.collectAsState()
    val pickFolderLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val treeUri = result.data?.data ?: return@rememberLauncherForActivityResult
            // Берём и сохраняем права на чтение (и, при необходимости, на запись) навсегда
            val takeFlags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION

            context.contentResolver.takePersistableUriPermission(treeUri, takeFlags)
            settingsVm.onRootAdded(treeUri)
        }
    }
    BackHandler {
        onDone() // при системном «назад» закрываем экран настроек
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(if (user != null) "My Profile" else "Sign In / Register",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.clickable(onClick = {
                if (user != null) {
                    nav.navigate(Navigation.Profile.route)
                } else {
                    nav.navigate(Navigation.Auth.route)
                }
            })
        )
        HorizontalDivider(Modifier.padding(vertical = 16.dp))
        Text("Appearance", style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(Modifier.padding(vertical = 16.dp))
        Text("Notifications", style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(Modifier.padding(vertical = 16.dp))
        Text("Cache", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))

        // --- Блок корней ---
        Spacer(Modifier.height(24.dp))
        Text("Корневые папки:", style = MaterialTheme.typography.titleMedium)
        if (ui.roots.isEmpty()) {
            Text("Нет выбранных папок", Modifier.padding(4.dp))
        } else {
            ui.roots.forEach { uri ->
                val name = uri.lastPathSegment.orEmpty()
                Text(
                    text = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        // двойной кликабельный: обычный и долгий
                        .combinedClickable(
                            onClick = { /* можно открыть превью или ничего */ },
                            onLongClick = { toDelete = uri }
                        )
                )
            }
        }

        Button(onClick = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            pickFolderLauncher.launch(intent) },
            modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Добавить папку")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                settingsVm.onScanClicked() },
            enabled = ui.roots.isNotEmpty() && !ui.isScanning
        ) {
            if (ui.isScanning) CircularProgressIndicator(Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text("Сканировать библиотеку")
        }

        ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }

    // — Dialog подтверждения удаления —
    toDelete?.let {
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Удалить корневую папку?") },
            text = { Text("Вы действительно хотите убрать «${toDelete!!.lastPathSegment}» из корней?") },
            confirmButton = {
                TextButton(onClick = {
                    settingsVm.onRootRemoved(toDelete!!)
                    toDelete = null
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}
