package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.data.local.SongEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSong = MutableStateFlow<SongEntity?>(null)
    val currentSong: StateFlow<SongEntity?> = _currentSong.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _playbackError = MutableStateFlow<String?>(null)
    val playbackError: StateFlow<String?> = _playbackError.asStateFlow()

    private var playlist: List<SongEntity> = emptyList()
    private var currentIndex = -1

    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun setPlaylist(songs: List<SongEntity>) {
        this.playlist = songs
        updateCurrentIndex()
    }

    private fun updateCurrentIndex() {
        val current = _currentSong.value
        if (current != null) {
            currentIndex = playlist.indexOfFirst { it.id == current.id }
        }
    }

    fun playSong(song: SongEntity) {
        try {
            _playbackError.value = null
            stopAndRelease()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, Uri.parse(song.url))
                setOnPreparedListener { mp ->
                    mp.start()
                    _isPlaying.value = true
                    _duration.value = mp.duration.toLong()
                    startProgressTracker()
                }
                setOnCompletionListener {
                    _isPlaying.value = false
                    skipNext()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayerManager", "Error occurred: what=$what extra=$extra")
                    _playbackError.value = "Unable to play track (network or codec issue)"
                    _isPlaying.value = false
                    false
                }
                prepareAsync()
            }

            _currentSong.value = song
            updateCurrentIndex()
        } catch (e: Exception) {
            Log.e("MediaPlayerManager", "Error in playSong", e)
            _playbackError.value = e.localizedMessage ?: "Playback initiation failed"
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
            stopProgressTracker()
        } else {
            player.start()
            _isPlaying.value = true
            startProgressTracker()
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.let {
            it.seekTo(positionMs.toInt())
            _currentPosition.value = positionMs
        }
    }

    fun skipNext() {
        if (playlist.isEmpty()) return
        if (currentIndex < playlist.size - 1) {
            currentIndex++
            playSong(playlist[currentIndex])
        } else if (playlist.isNotEmpty()) {
            currentIndex = 0
            playSong(playlist[0])
        }
    }

    fun skipPrevious() {
        if (playlist.isEmpty()) return
        if (currentIndex > 0) {
            currentIndex--
            playSong(playlist[currentIndex])
        } else {
            currentIndex = playlist.size - 1
            playSong(playlist[currentIndex])
        }
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _currentPosition.value = it.currentPosition.toLong()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun stopAndRelease() {
        stopProgressTracker()
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0L
    }

    fun release() {
        stopAndRelease()
        scope.cancel()
    }
}
