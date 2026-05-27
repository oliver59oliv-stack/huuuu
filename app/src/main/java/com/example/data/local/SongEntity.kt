package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val url: String,
    val artworkUrl: String,
    val genre: String,
    val isLiked: Boolean = false,
    val orderIndex: Int = 0
)
