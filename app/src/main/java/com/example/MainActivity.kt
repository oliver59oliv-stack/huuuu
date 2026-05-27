package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomPlayerBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MusicViewModel
import com.example.ui.viewmodel.NavigationScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MusicViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()
                val isPlayerFullScreen by viewModel.isPlayerFullScreen.collectAsState()
                val currentSong by viewModel.playerManager.currentSong.collectAsState()

                var lang by remember { mutableStateOf("ar") }
                val isRTL = lang == "ar"

                val configuration = LocalConfiguration.current
                val isTablet = configuration.screenWidthDp >= 720

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                @Composable
                fun SidebarContent(
                    activeScreen: NavigationScreen,
                    onScreenSelect: (NavigationScreen) -> Unit,
                    onLangToggle: () -> Unit,
                    onClose: (() -> Unit)? = null
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val angle by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(6000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(260.dp)
                            .background(DarkCarbon)
                            .border(
                                width = if (isRTL) 0.dp else 1.dp,
                                brush = Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Album,
                                        contentDescription = "Spinning logo",
                                        tint = NeonEmerald,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .rotate(angle)
                                    )
                                    Column {
                                        Text(
                                            text = "AURA SOUNDS",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "أثير • Live Streams",
                                            color = NeonEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (onClose != null) {
                                    IconButton(
                                        onClick = onClose,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(50))
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Close navigation", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            val menuItems = listOf(
                                Triple(NavigationScreen.HOME, if (isRTL) "الرئيسية" else "Home Dashboard", Icons.Default.Home),
                                Triple(NavigationScreen.LIBRARY, if (isRTL) "مكتبتي الموسيقية" else "My Library", Icons.Default.LibraryMusic),
                                Triple(NavigationScreen.RADIO, if (isRTL) "الراديو المباشر" else "Live Radio Grid", Icons.Default.Radio),
                                Triple(NavigationScreen.UPLOAD, if (isRTL) "تحميل الموسيقى" else "Cloud Uploader", Icons.Default.CloudUpload),
                                Triple(NavigationScreen.ADD_RADIO, if (isRTL) "إعداد البث" else "Radio Manager", Icons.Default.PlaylistAdd),
                                Triple(NavigationScreen.YOUTUBE, if (isRTL) "يوتيوب ميديا" else "YouTube Direct", Icons.Default.Slideshow)
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                menuItems.forEach { (screen, label, icon) ->
                                    val isSelected = activeScreen == screen
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) NeonEmerald.copy(alpha = 0.12f) else Color.Transparent)
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) NeonEmerald.copy(alpha = 0.2f) else Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable { onScreenSelect(screen) }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            tint = if (isSelected) NeonEmerald else Color.White.copy(alpha = 0.6f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = label,
                                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.04f))
                                    .clickable { onLangToggle() }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Language, contentDescription = "Language toggle", tint = NeonEmerald, modifier = Modifier.size(14.dp))
                                    Text(if (isRTL) "العربية" else "English", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White.copy(alpha = 0.3f), modifier = Modifier.size(10.dp))
                            }

                            Text(
                                text = "© 2026 AURA SOUNDS • أثير",
                                color = Color.White.copy(alpha = 0.3f),
                                fontSize = 9.sp,
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }
                    }
                }

                @Composable
                fun MainScaffoldContent() {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MidnightDb,
                        bottomBar = {
                            if (!isTablet) {
                                Column {
                                    if (currentSong != null) {
                                        BottomPlayerBar(viewModel = viewModel)
                                    }

                                    NavigationBar(
                                        containerColor = MidnightDb,
                                        tonalElevation = 8.dp,
                                        modifier = Modifier
                                            .navigationBarsPadding()
                                            .testTag("bottom_nav_bar")
                                    ) {
                                        NavigationBarItem(
                                            selected = currentScreen == NavigationScreen.HOME,
                                            onClick = { viewModel.setScreen(NavigationScreen.HOME) },
                                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                            label = { Text(if (isRTL) "الرئيسية" else "Home", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = CyberCyan,
                                                indicatorColor = CyberCyan,
                                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                                            ),
                                            modifier = Modifier.testTag("nav_home_tab")
                                        )

                                        NavigationBarItem(
                                            selected = currentScreen == NavigationScreen.LIBRARY,
                                            onClick = { viewModel.setScreen(NavigationScreen.LIBRARY) },
                                            icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Library") },
                                            label = { Text(if (isRTL) "المكتبة" else "Library", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = CyberCyan,
                                                indicatorColor = CyberCyan,
                                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                                            ),
                                            modifier = Modifier.testTag("nav_library_tab")
                                        )

                                        NavigationBarItem(
                                            selected = currentScreen == NavigationScreen.YOUTUBE,
                                            onClick = { viewModel.setScreen(NavigationScreen.YOUTUBE) },
                                            icon = { Icon(Icons.Default.Slideshow, contentDescription = "YouTube") },
                                            label = { Text("YouTube", fontSize = 10.sp) },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color.Black,
                                                selectedTextColor = CyberCyan,
                                                indicatorColor = CyberCyan,
                                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                                unselectedTextColor = Color.White.copy(alpha = 0.5f)
                                            ),
                                            modifier = Modifier.testTag("nav_youtube_tab")
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .padding(bottom = innerPadding.calculateBottomPadding())
                        ) {
                            CompositionLocalProvider(
                                androidx.compose.ui.platform.LocalLayoutDirection provides (if (isRTL) androidx.compose.ui.unit.LayoutDirection.Rtl else androidx.compose.ui.unit.LayoutDirection.Ltr)
                            ) {
                                when (currentScreen) {
                                    NavigationScreen.HOME -> HomeScreen(
                                        viewModel = viewModel,
                                        lang = lang,
                                        onLangChange = { lang = it },
                                        onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                    )
                                    NavigationScreen.LIBRARY, NavigationScreen.MUSIC -> LibraryScreen(viewModel = viewModel)
                                    NavigationScreen.RADIO -> RadioScreen(viewModel = viewModel, lang = lang)
                                    NavigationScreen.UPLOAD -> UploadScreen(viewModel = viewModel, lang = lang)
                                    NavigationScreen.ADD_RADIO -> {
                                        Box(modifier = Modifier.fillMaxSize().background(MidnightDb).padding(bottom = 12.dp)) {
                                            ManageRadiosTab(
                                                isRTL = isRTL,
                                                onPlayRadio = { name, url, logo ->
                                                    val rSong = com.example.data.local.SongEntity(
                                                        id = "radio_" + name.hashCode(),
                                                        title = name,
                                                        artist = "LIVE Broadcast • البث المباشر",
                                                        album = "Global Stations",
                                                        duration = 0,
                                                        url = url,
                                                        artworkUrl = logo ?: "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?q=80&w=400",
                                                        genre = "Live Radio"
                                                    )
                                                    viewModel.playerManager.playSong(rSong)
                                                }
                                            )
                                        }
                                    }
                                    NavigationScreen.YOUTUBE -> YoutubeScreen(viewModel = viewModel, lang = lang)
                                    NavigationScreen.SEARCH -> SearchScreen(viewModel = viewModel)
                                    else -> HomeScreen(
                                        viewModel = viewModel,
                                        lang = lang,
                                        onLangChange = { lang = it },
                                        onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MidnightDb)
                ) {
                    if (isTablet) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            SidebarContent(
                                activeScreen = currentScreen,
                                onScreenSelect = { viewModel.setScreen(it) },
                                onLangToggle = { lang = if (lang == "ar") "en" else "ar" }
                            )

                            Box(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                                MainScaffoldContent()
                            }
                        }
                    } else {
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            gesturesEnabled = true,
                            drawerContent = {
                                SidebarContent(
                                    activeScreen = currentScreen,
                                    onScreenSelect = { screen ->
                                        viewModel.setScreen(screen)
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onLangToggle = { lang = if (lang == "ar") "en" else "ar" },
                                    onClose = { coroutineScope.launch { drawerState.close() } }
                                )
                            }
                        ) {
                            MainScaffoldContent()
                        }
                    }

                    AnimatedVisibility(
                        visible = isPlayerFullScreen,
                        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }) + fadeOut()
                    ) {
                        PlayerScreen(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
