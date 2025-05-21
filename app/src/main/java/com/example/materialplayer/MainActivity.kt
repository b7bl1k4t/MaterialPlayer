package com.example.materialplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.materialplayer.ui.screens.MainScreen
import com.example.materialplayer.ui.theme.MaterialPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialPlayerTheme {
                MainScreen()
            }
        }
    }
}



