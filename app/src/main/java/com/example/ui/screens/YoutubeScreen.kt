package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon
import com.example.ui.viewmodel.MusicViewModel

@Composable
fun YoutubeScreen(
    viewModel: MusicViewModel,
    lang: String = "ar",
    modifier: Modifier = Modifier
) {
    val isRTL = lang == "ar"
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    val mockYoutubeResults = remember {
        listOf(
            Pair("Lofi Girl - Chill Beats live 24/7", "https://radio-worker.ma68.workers.dev/songs/demo_1.mp3"),
            Pair("Synthwave Electro Beats 1 Hour", "https://radio-worker.ma68.workers.dev/songs/demo_2.mp3"),
            Pair("Nature Rain Sounds Sleep Relax", "https://radio-worker.ma68.workers.dev/songs/demo_3.mp3")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDb)
            .padding(16.dp)
    ) {
        Text(
            text = if (isRTL) "البث المباشر عبر يوتيوب" else "Stream YouTube Music Directly",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = if (isRTL) "ابحث عن فيديوهات وقنوات الموسيقى المفضلة لديك" else "Instant video stream to audio conversion receiver Hub",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text(if (isRTL) "ابحث عن ملف صوتي على يوتيوب..." else "Search YouTube streams...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SoftCarbon
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { /* Search Trigger */ },
                modifier = Modifier
                    .size(48.dp)
                    .background(CyberCyan, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(mockYoutubeResults) { (title, url) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftCarbon)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Slideshow,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text("YouTube Audio stream", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                        }
                    }

                    IconButton(
                        onClick = {
                            viewModel.addSong(
                                title = title,
                                artist = "YouTube Streamer",
                                album = "YouTube Stream",
                                url = url,
                                artworkUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?q=80&w=400",
                                genre = "YouTube Bookmark"
                            )
                            Toast.makeText(context, "Added YouTube Stream to Library!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Bookmark", tint = NeonEmerald)
                    }
                }
            }
        }
    }
}
