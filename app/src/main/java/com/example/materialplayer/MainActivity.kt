package com.example.materialplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.materialplayer.ui.composables.BottomBar
import com.example.materialplayer.ui.navigation.Navigation
import com.example.materialplayer.ui.screens.AlbumDetailScreen
import com.example.materialplayer.ui.screens.ArtistDetailScreen
import com.example.materialplayer.ui.screens.LibraryScreen
import com.example.materialplayer.ui.screens.PlaylistDetailScreen
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
                val navController = rememberNavController()

                Scaffold(bottomBar = { BottomBar(navController) }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Navigation.Library.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(Navigation.Library.route) {
                            LibraryScreen(
                                nav = navController,
                                onNavigateToSettings = { navController.navigate(Navigation.Settings.route){
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
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
                        composable(Navigation.Playlists.route) { PlaylistsScreen(nav = navController) }
                        composable(Navigation.Search.route) { SearchScreen(nav = navController) }
                        composable(Navigation.Settings.route) {
                            SettingsScreen(
                                onDone = { navController.popBackStack(
                                    route = Navigation.Library.route,
                                    inclusive = false
                                ) }
                            )
                        }
                        composable(
                            "album/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { AlbumDetailScreen(navController) }
                        composable(
                            "artist/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { ArtistDetailScreen(navController) }
                        composable(
                            route = "playlist/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { backEntry ->
                            val id = backEntry.arguments!!.getLong("id")
                            PlaylistDetailScreen(id = id, nav = navController)
                        }
                    }
                }
            }
        }
    }
}




