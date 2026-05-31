package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.player.MediaPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val category: String,
    val imageUrl: String = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=200"
)

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val playerManager = MediaPlayerManager(application)
    
    val isPlaying = playerManager.isPlaying
    val currentTrackUri = playerManager.currentTrack

    private val _tracks = MutableStateFlow(listOf(
        Track("1", "Chill Lofi Lounge", "Retro Breeze", "Lo-Fi"),
        Track("2", "Cosmic Wave Synth", "Neon Horizon", "Synthwave"),
        Track("3", "Midnight Coffee Cafe", "Ambient Coffee", "Jazz"),
        Track("4", "Summer Chill Waves", "Aura Sounds Lab", "Favorites")
    ))
    val tracks = _tracks.asStateFlow()

    private val _categories = MutableStateFlow(listOf("All Tracks", "Favorites", "Synthwave", "Lo-Fi"))
    val categories = _categories.asStateFlow()

    fun playTrack(track: Track) {
        // In a real app, this would be a real URL
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
