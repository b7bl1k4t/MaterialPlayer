package com.example.materialplayer.ui.viewmodel

import android.net.Uri
import android.provider.DocumentsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.materialplayer.data.preferences.RootsPreferences
import com.example.materialplayer.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val roots: List<Uri> = emptyList(),
    val isScanning: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val rootsPrefs: RootsPreferences,
    private val repo: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            rootsPrefs.rootsFlow
                .onEach { roots -> _uiState.update { it.copy(roots = roots) } }
                .launchIn(this)
        }
    }

    /** Пользователь добавил новый корень через SAF */
    fun onRootAdded(uri: Uri) {
        viewModelScope.launch {
            val docUri = DocumentsContract.buildDocumentUriUsingTree(
                uri,
                DocumentsContract.getTreeDocumentId(uri)
            )
            rootsPrefs.addRoot(docUri)        //  ⬅  вместо treeUri
        }
    }

    fun onRootRemoved(uri: Uri) {
        viewModelScope.launch {
            rootsPrefs.removeRoot(uri)
        }
    }

    /** Полный рескан библиотеки по всем сохранённым корням */
    fun onScanClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, error = null) }
            try {
                val roots = rootsPrefs.rootsFlow.first()
                repo.scanLibrary(roots)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isScanning = false) }
            }
        }
    }
}
