package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GlowRuby
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon
import com.example.ui.viewmodel.MusicViewModel

@Composable
fun PlayerScreen(
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    val currentSong by viewModel.playerManager.currentSong.collectAsState()
    val isPlaying by viewModel.playerManager.isPlaying.collectAsState()
    val currentPos by viewModel.playerManager.currentPosition.collectAsState()
    val duration by viewModel.playerManager.duration.collectAsState()

    val song = currentSong ?: return

    // Audio Visualizer Simulator values
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SoftCarbon, MidnightDb)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper action row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.setPlayerFullScreen(false) },
                modifier = Modifier.testTag("close_player_screen")
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dismiss Player",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "NOW BROADCASTING",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            IconButton(onClick = { viewModel.toggleFavorite(song) }) {
                Icon(
                    imageVector = if (song.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (song.isLiked) GlowRuby else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Center spinning album artwork
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .border(6.dp, SoftCarbon, CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = song.artworkUrl,
                contentDescription = "Spinning artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .rotate(if (isPlaying) angle else 0f)
            )
            // Center vinyl core hole
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MidnightDb)
                    .border(2.dp, SoftCarbon, CircleShape)
            )
        }

        // Track Details & Custom Visualizer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.testTag("player_fullscreen_title")
            )
            Text(
                text = song.artist,
                color = CyberCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Beautiful Simulated Frequency Spectrum Visualizer Block
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(36.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom
            ) {
                val heights = listOf(waveHeight1, waveHeight2, waveHeight1 * 0.7f, waveHeight3, waveHeight2 * 0.6f, waveHeight1)
                heights.forEach { h ->
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .fillMaxHeight(if (isPlaying) h else 0.15f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(NeonEmerald)
                    )
                }
            }
        }

        // Timeline slider and counter layout
        Column(modifier = Modifier.fillMaxWidth()) {
            val sliderPos = if (duration > 0) currentPos.toFloat() / duration.toFloat() else 0f
            Slider(
                value = sliderPos,
                onValueChange = { percent ->
                    val targetPos = (percent * duration).toLong()
                    viewModel.playerManager.seekTo(targetPos)
                },
                colors = SliderDefaults.colors(
                    activeTrackColor = CyberCyan,
                    inactiveTrackColor = SoftCarbon,
                    thumbColor = NeonEmerald
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPos),
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
                Text(
                    text = formatTime(duration),
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }
        }

        // Play/Pause and Skip Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.playerManager.skipPrevious() },
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { viewModel.playerManager.togglePlayPause() },
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(CyberCyan)
                    .testTag("player_fullscreen_play_pause")
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(44.dp)
                )
            }

            IconButton(
                onClick = { viewModel.playerManager.skipNext() },
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return String.format("%02d:%02d", min, sec)
}
