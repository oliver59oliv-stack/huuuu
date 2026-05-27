package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SongEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon
import com.example.ui.viewmodel.MusicViewModel

@Composable
fun RadioScreen(
    viewModel: MusicViewModel,
    lang: String = "ar",
    modifier: Modifier = Modifier
) {
    val isRTL = lang == "ar"

    val stations = listOf(
        Triple("Chillout FM", "https://radio-worker.ma68.workers.dev/songs/demo_1.mp3", "Global Down-tempo"),
        Triple("Synthetix Cyber", "https://radio-worker.ma68.workers.dev/songs/demo_2.mp3", "80s Retro Arcade"),
        Triple("Capital Hit Radio", "https://radio-worker.ma68.workers.dev/songs/demo_3.mp3", "Direct Broadcast 1"),
        Triple("Aura Live Ambient", "https://radio-worker.ma68.workers.dev/songs/demo_4.mp3", "Calm Nature Waves Space")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDb)
            .padding(16.dp)
    ) {
        Text(
            text = if (isRTL) "الراديو والمحطات المباشرة" else "World Airwaves",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = if (isRTL) "بثوث فورية حية ومباشرة من جميع أنحاء العالم" else "Access immediate high-fidelity live audio stations",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(stations) { (name, url, desc) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SoftCarbon),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val rSong = SongEntity(
                                id = "radio_${name.hashCode()}",
                                title = name,
                                artist = "LIVE STATION",
                                album = desc,
                                duration = 0,
                                url = url,
                                artworkUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?q=80&w=400",
                                genre = "Live Radio"
                            )
                            viewModel.playerManager.playSong(rSong)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(CyberCyan.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Radio,
                                contentDescription = null,
                                tint = NeonEmerald,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = desc,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
