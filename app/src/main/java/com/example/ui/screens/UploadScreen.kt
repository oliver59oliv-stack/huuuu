package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon
import com.example.ui.viewmodel.MusicViewModel

@Composable
fun UploadScreen(
    viewModel: MusicViewModel,
    lang: String = "ar",
    modifier: Modifier = Modifier
) {
    val isRTL = lang == "ar"
    val context = LocalContext.current

    var songTitle by remember { mutableStateOf("") }
    var songArtist by remember { mutableStateOf("") }
    var audioUrl by remember { mutableStateOf("") }
    var artworkUrl by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }

    val presetUploads = listOf(
        Pair("Ambient Forest Echoes", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"),
        Pair("Cyber Midnight Synth", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"),
        Pair("Chill Acoustic Guitar", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDb)
            .padding(16.dp)
    ) {
        Text(
            text = if (isRTL) "تحميل الموسيقى السحابية" else "Cloud Music Importer",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = if (isRTL) "أدخل رابط ملف صوتي خارجي لحفظه في مكتبتك وتسهيل تشغيله" else "Import online audio tracks into your client library database",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = if (isRTL) "استيراد سريع ومبسط" else "POPULAR ROYALTY FREE SEEDS",
            color = Color.White.copy(alpha = 0.35f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        presetUploads.forEach { (title, url) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftCarbon)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(url, color = Color.White.copy(alpha = 0.4f), fontSize = 9.sp, maxLines = 1)
                }

                IconButton(
                    onClick = {
                        viewModel.addSong(
                            title = title,
                            artist = "SoundHelix Inc",
                            album = "Cloud Presets",
                            url = url,
                            artworkUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?q=80&w=400",
                            genre = "Importers"
                        )
                        Toast.makeText(context, "Added Preset to Local Library!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isRTL) "أو أدخل بيانات رابط مخصص:" else "OR IMPORT SPECIFIC RESOURCE:",
            color = Color.White.copy(alpha = 0.35f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = songTitle,
                onValueChange = { songTitle = it },
                label = { Text(if (isRTL) "عنوان الأغنية" else "Track Title") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SoftCarbon,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().testTag("upload_title_field")
            )

            OutlinedTextField(
                value = audioUrl,
                onValueChange = { audioUrl = it },
                label = { Text(if (isRTL) "رابط الملف الصوتي المباشر (Direct Audio MP3 Link)" else "Audio Stream URL (mp3)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SoftCarbon,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().testTag("upload_url_field")
            )

            Button(
                onClick = {
                    if (songTitle.isNotBlank() && audioUrl.isNotBlank()) {
                        viewModel.addSong(
                            title = songTitle,
                            artist = songArtist.ifBlank { "Unidentified Artist" },
                            album = "External Stream",
                            url = audioUrl,
                            artworkUrl = artworkUrl,
                            genre = genre.ifBlank { "Cloud Music" }
                        )
                        Toast.makeText(context, "Successfully Imported Link!", Toast.LENGTH_SHORT).show()
                        songTitle = ""
                        songArtist = ""
                        audioUrl = ""
                        artworkUrl = ""
                        genre = ""
                    } else {
                        Toast.makeText(context, "Title and Audio URL are required", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                modifier = Modifier.fillMaxWidth().testTag("submit_uploaded_song_btn")
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isRTL) "حفظ في المكتبة والتشغيل" else "Commit & Save to Library", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
