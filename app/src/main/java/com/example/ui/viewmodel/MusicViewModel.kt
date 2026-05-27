package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MusicDatabase
import com.example.data.local.SongEntity
import com.example.data.repository.MusicRepository
import com.example.player.MediaPlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MusicDatabase.getDatabase(application)
    val repository = MusicRepository(db.musicDao())

    val playerManager = MediaPlayerManager(application)

    val songs: StateFlow<List<SongEntity>> = repository.allSongs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow(NavigationScreen.HOME)
    val currentScreen: StateFlow<NavigationScreen> = _currentScreen.asStateFlow()

    private val _isPlayerFullScreen = MutableStateFlow(false)
    val isPlayerFullScreen: StateFlow<Boolean> = _isPlayerFullScreen.asStateFlow()

    init {
        // Automatically sync database songs to player when they change
        viewModelScope.launch {
            songs.collect { playlist ->
                playerManager.setPlaylist(playlist)
            }
        }

        // Seed some demo songs if empty
        viewModelScope.launch(Dispatchers.IO) {
            if (db.musicDao().getSongById("demo_1") == null) {
                val demoSongs = listOf(
                    SongEntity(
                        id = "demo_1",
                        title = "Chill Lofi Lounge",
                        artist = "Retro Breeze",
                        album = "Acoustic Spaces",
                        duration = 372,
                        url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                        artworkUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?q=80&w=400",
                        genre = "Lo-Fi",
                        isLiked = false,
                        orderIndex = 0
                    ),
                    SongEntity(
                        id = "demo_2",
                        title = "Cosmic Wave Synth",
                        artist = "Neon Horizon",
                        album = "Cyber Arcade",
                        duration = 423,
                        url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                        artworkUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?q=80&w=400",
                        genre = "Synthwave",
                        isLiked = true,
                        orderIndex = 1
                    ),
                    SongEntity(
                        id = "demo_3",
                        title = "Midnight Coffee Cafe",
                        artist = "Ambient Coffee",
                        album = "Evening Chillout",
                        duration = 302,
                        url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                        artworkUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=400",
                        genre = "Ambience",
                        isLiked = false,
                        orderIndex = 2
                    ),
                    SongEntity(
                        id = "demo_4",
                        title = "Summer Chill Waves",
                        artist = "Aura Sounds Lab",
                        album = "Island Sunlight",
                        duration = 288,
                        url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
                        artworkUrl = "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?q=80&w=400",
                        genre = "Chill",
                        isLiked = false,
                        orderIndex = 3
                    )
                )
                for (song in demoSongs) {
                    repository.insertSong(song)
                }
            }
        }
    }

    fun setScreen(screen: NavigationScreen) {
        _currentScreen.value = screen
    }

    fun setPlayerFullScreen(fullscreen: Boolean) {
        _isPlayerFullScreen.value = fullscreen
    }

    fun toggleFavorite(song: SongEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(song.id, !song.isLiked)
        }
    }

    fun addSong(title: String, artist: String, album: String, url: String, artworkUrl: String, genre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = "user_${System.currentTimeMillis()}"
            val newSong = SongEntity(
                id = id,
                title = title,
                artist = artist,
                album = album,
                duration = 240,
                url = url,
                artworkUrl = artworkUrl.ifBlank { "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?q=80&w=400" },
                genre = genre
            )
            repository.insertSong(newSong)
        }
    }

    fun deleteSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSongById(songId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
