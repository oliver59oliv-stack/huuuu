package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon
import com.example.ui.viewmodel.MusicViewModel

@Composable
fun SearchScreen(
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    val songs by viewModel.songs.collectAsState()
    var searchTxt by remember { mutableStateOf("") }

    val results = remember(songs, searchTxt) {
        if (searchTxt.isBlank()) emptyList()
        else songs.filter {
            it.title.contains(searchTxt, ignoreCase = true) ||
            it.artist.contains(searchTxt, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDb)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchTxt,
            onValueChange = { searchTxt = it },
            placeholder = { Text("Search songs or artists...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.5f)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = SoftCarbon
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Type query above to show matches", color = Color.White.copy(alpha = 0.4f), fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftCarbon)
                            .clickable { viewModel.playerManager.playSong(song) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(song.title, color = Color.White, fontSize = 13.sp)
                            Text(song.artist, color = CyberCyan, fontSize = 11.sp)
                        }
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NeonEmerald)
                    }
                }
            }
        }
    }
}
