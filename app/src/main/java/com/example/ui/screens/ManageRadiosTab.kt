package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MidnightDb
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SoftCarbon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageRadiosTab(
    isRTL: Boolean = false,
    onPlayRadio: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var radioName by remember { mutableStateOf("") }
    var radioUrl by remember { mutableStateOf("") }
    var logoUrl by remember { mutableStateOf("") }

    val savedRadios = remember {
        mutableStateListOf(
            Triple("Retro Beats FM", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?q=80&w=400"),
            Triple("Classic Synth FM", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", null)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightDb)
            .padding(16.dp)
    ) {
        Text(
            text = if (isRTL) "بثوث الراديو المخصصة" else "CUSTOM LIVE RADIOS",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Text(
            text = if (isRTL) "أضف روابط بث مباشرة مخصصة وشغلها فوراً" else "Type stream URLs below to configure personal airwave receivers",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Fields
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = radioName,
                onValueChange = { radioName = it },
                label = { Text(if (isRTL) "اسم القناة" else "Station Name") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SoftCarbon,
                    focusedLabelColor = CyberCyan,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = radioUrl,
                onValueChange = { radioUrl = it },
                label = { Text(if (isRTL) "رابط البث (URL)" else "Stream URL (MP3/AAC)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = SoftCarbon,
                    focusedLabelColor = CyberCyan,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (radioName.isNotBlank() && radioUrl.isNotBlank()) {
                        savedRadios.add(Triple(radioName, radioUrl, logoUrl.ifBlank { null }))
                        radioName = ""
                        radioUrl = ""
                        logoUrl = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isRTL) "إضافة جديد" else "Register Receiver", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grid/List of Custom Radios
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(savedRadios.size) { index ->
                val (name, url, logo) = savedRadios[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SoftCarbon)
                        .clickable { onPlayRadio(name, url, logo) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(50))
                                .background(CyberCyan.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Radio, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text(name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(url, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, maxLines = 1)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonEmerald)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(if (isRTL) "بث" else "Tune", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
