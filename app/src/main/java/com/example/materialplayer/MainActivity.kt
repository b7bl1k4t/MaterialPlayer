package com.example.materialplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.materialplayer.ui.composables.BottomBar
import com.example.materialplayer.ui.navigation.Navigation
import com.example.materialplayer.ui.screens.LibraryScreen
import com.example.materialplayer.ui.screens.PlaylistsScreen
import com.example.materialplayer.ui.screens.SearchScreen
import com.example.materialplayer.ui.screens.SettingsScreen
import com.example.materialplayer.ui.theme.MaterialPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialPlayerTheme {
                val nav = rememberNavController()

                Scaffold(bottomBar = { BottomBar(nav) }) { innerPadding ->
                    NavHost(
                        navController = nav,
                        startDestination = Navigation.Library.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(Navigation.Library.route) {
                            LibraryScreen(
                                onNavigateToSettings = { nav.navigate(Navigation.Settings.route){
                                    popUpTo(nav.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                } }
                            )
                        }
//                        composable(Navigation.NowPlaying.route) {
//                            NowPlayingScreen(
//                                exoPlayer = hiltViewModel<LibraryViewModel>().exoPlayer,
//                                onBack = { nav.popBackStack() }
//                            )
//                        }
                        composable(Navigation.Playlists.route) { PlaylistsScreen() }
                        composable(Navigation.Search.route) { SearchScreen() }
                        composable(Navigation.Settings.route) {
                            SettingsScreen(
                                onDone = { nav.popBackStack(
                                    route = Navigation.Library.route,
                                    inclusive = false
                                ) }
                            )
                        }
                    }
                }
            }
        }
    }
}




