package com.example.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentTrack = MutableStateFlow<String?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    fun play(uri: String) {
        mediaPlayer?.release()
        _currentTrack.value = uri
        mediaPlayer = MediaPlayer().apply {
            // Using a sample local resource or URL logic
            // For now, we'll just handle the URI
            try {
                setDataSource(context, Uri.parse(uri))
                prepare()
                start()
                _isPlaying.value = true
                setOnCompletionListener {
                    _isPlaying.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            } else {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
