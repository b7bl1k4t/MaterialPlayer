package com.example.materialplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.materialplayer.ui.composables.BottomBar
import com.example.materialplayer.ui.composables.MiniPlayerHost
import com.example.materialplayer.ui.navigation.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            Column {
                MiniPlayerHost(navController)
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.Library.route,  // начальный экран
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Navigation.Library.route) {
                LibraryScreen(
                    nav = navController,
                    onNavigateToSettings = {
                        navController.navigate(Navigation.Settings.route)
                    }
                )
            }
            composable(Navigation.NowPlaying.route) {
                NowPlayingScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Navigation.Playlists.route) { PlaylistsScreen(nav = navController) }
            composable(Navigation.Search.route) { SearchScreen(nav = navController) }
            composable(Navigation.Settings.route) {
                SettingsScreen(
                    nav = navController,
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
                PlaylistDetailScreen(nav = navController)
            }
            composable(Navigation.Auth.route) {
                AuthScreen(
                    onAuthSuccess = { user -> /* обработка успешной аутентификации */ },
                    onAuthFailure = { exception -> /* обработка ошибки аутентификации */ }
                )
            }
            composable(Navigation.Profile.route) {
                ProfileScreen(nav = navController)
            }
        }
    }
}