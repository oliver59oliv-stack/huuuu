package com.example.data.repository

import com.example.data.local.MusicDao
import com.example.data.local.SongEntity
import kotlinx.coroutines.flow.Flow

class MusicRepository(private val musicDao: MusicDao) {
    val allSongs: Flow<List<SongEntity>> = musicDao.getAllSongs()

    suspend fun getSongById(songId: String): SongEntity? {
        return musicDao.getSongById(songId)
    }

    suspend fun insertSong(song: SongEntity) {
        musicDao.insertSong(song)
    }

    suspend fun updateSong(song: SongEntity) {
        musicDao.updateSong(song)
    }

    suspend fun deleteSongById(songId: String) {
        musicDao.deleteSongById(songId)
    }

    suspend fun updateFavoriteStatus(songId: String, isLiked: Boolean) {
        musicDao.updateFavoriteStatus(songId, isLiked)
    }
}
