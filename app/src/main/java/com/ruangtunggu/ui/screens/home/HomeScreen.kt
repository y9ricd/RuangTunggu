package com.ruangtunggu.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTHeaderWave
import com.ruangtunggu.ui.components.RTQueueCard
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandMint
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal
import com.ruangtunggu.ui.theme.RTBrandTealSurface

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPoli by remember { mutableStateOf("Umum") }
    var currentTab by remember { mutableStateOf("Beranda") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            FigmaBottomBar(
                currentTab = currentTab,
                onTabSelect = { tab ->
                    currentTab = tab
                    when (tab) {
                        "Antrean" -> navController.navigate(Routes.QueueMonitor("active_reg"))
                        "Riwayat" -> navController.navigate(Routes.History)
                        "Profil" -> navController.navigate(Routes.Profile)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── 1. Top Teal Header with Wave, Greeting & Floating Search ────
            RTHeaderWave(
                modifier = Modifier.fillMaxWidth(),
                height = 240.dp,
                waveDip = 0.86f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // Greeting Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Initials Avatar Circle
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.28f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "NP",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${uiState.greetingText.ifEmpty { "Selamat pagi" }} 👋",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                                Text(
                                    text = uiState.patientName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Notification Button with Red Dot Badge
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.22f))
                                    .clickable { navController.navigate(Routes.Notifications) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifikasi",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // Red notification dot
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-2).dp, y = 2.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Floating Search Bar Pill inside Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(26.dp),
                                spotColor = Color(0x1F000000)
                            )
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color.White)
                            .clickable { navController.navigate(Routes.Search()) }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = RTBrandMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Cari rumah sakit atau poli...",
                            fontSize = 14.sp,
                            color = RTBrandMuted
                        )
                    }
                }
            }

            // ── 2. Active Queue Card (Overlaps the bottom of the header wave) ─
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .padding(horizontal = 20.dp)
            ) {
                RTQueueCard(
                    queueNumber = "A-023",
                    currentServing = "A-018",
                    poliName = "Poli Anak",
                    hospitalName = "RS Sehat Sidoarjo",
                    date = "Kam, 22 Okt · 09:00",
                    remainingCount = 5,
                    estimatedMinutes = 25,
                    onMonitorClick = {
                        navController.navigate(Routes.QueueMonitor("active_reg"))
                    }
                )
            }

            // ── 3. Layanan (4 Rounded Squircle Quick Actions) ───────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Layanan",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Cari RS
                    FigmaServiceButton(
                        label = "Cari RS",
                        icon = Icons.Default.LocalHospital,
                        backgroundColor = Color(0xFFE6F7F5),
                        iconTint = RTBrandTeal,
                        onClick = { navController.navigate(Routes.Search()) }
                    )

                    // Jadwal
                    FigmaServiceButton(
                        label = "Jadwal",
                        icon = Icons.Default.CalendarMonth,
                        backgroundColor = Color(0xFFD1F4EC),
                        iconTint = RTBrandTeal,
                        onClick = { navController.navigate(Routes.SelectSchedule("all")) }
                    )

                    // Riwayat
                    FigmaServiceButton(
                        label = "Riwayat",
                        icon = Icons.Default.History,
                        backgroundColor = Color(0xFFE8FAF8),
                        iconTint = RTBrandTeal,
                        onClick = { navController.navigate(Routes.History) }
                    )

                    // Profil
                    FigmaServiceButton(
                        label = "Profil",
                        icon = Icons.Default.Person,
                        backgroundColor = Color(0xFFF1F5F9),
                        iconTint = Color(0xFF475569),
                        onClick = { navController.navigate(Routes.Profile) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── 4. Poli Populer ───────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Poli populer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )

                    Text(
                        text = "Lihat semua",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandTeal,
                        modifier = Modifier
                            .clickable { navController.navigate(Routes.Search()) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Umum", "Anak", "Gigi", "Kandungan", "Mata", "THT").forEach { poli ->
                        val isSelected = selectedPoli == poli
                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .clip(RoundedCornerShape(19.dp))
                                .background(if (isSelected) RTBrandTeal else Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) RTBrandTeal else RTBrandBorder,
                                    shape = RoundedCornerShape(19.dp)
                                )
                                .clickable { selectedPoli = poli }
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = poli,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF334155)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── 5. Rumah Sakit Terdekat ───────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Rumah sakit terdekat",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FigmaHospitalCard(
                        name = "RS Sehat Sidoarjo",
                        rating = "4.8",
                        distance = "1,2 km",
                        gradientColors = listOf(Color(0xFFE07A5F), Color(0xFFF4A261)),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.PoliDetail("rs_sehat_sidoarjo")) }
                    )

                    FigmaHospitalCard(
                        name = "RS Harapan Bunda",
                        rating = "4.8",
                        distance = "1,2 km",
                        gradientColors = listOf(Color(0xFF81B29A), Color(0xFF3D5A80)),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.PoliDetail("rs_harapan_bunda")) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/**
 * Service Button: Rounded squircle (64x64dp) with pastel background and icon.
 */
@Composable
private fun FigmaServiceButton(
    label: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = RTBrandNavy
        )
    }
}

/**
 * Hospital Card: Rounded white card (20dp) with thumbnail header and rating/distance.
 */
@Composable
private fun FigmaHospitalCard(
    name: String,
    rating: String,
    distance: String,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0x1F000000),
                ambientColor = Color(0x0F000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        Column {
            // Stylised Thumbnail Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Brush.linearGradient(colors = gradientColors)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandNavy,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$rating · $distance",
                        fontSize = 11.sp,
                        color = RTBrandSlate
                    )
                }
            }
        }
    }
}

/**
 * Bottom Bar matching Figma with pill active indicator for Beranda.
 */
@Composable
private fun FigmaBottomBar(
    currentTab: String,
    onTabSelect: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 14.dp, spotColor = Color(0x14000000))
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Beranda
            BottomNavTab(
                label = "Beranda",
                icon = if (currentTab == "Beranda") Icons.Default.Home else Icons.Outlined.Home,
                isSelected = currentTab == "Beranda",
                onClick = { onTabSelect("Beranda") }
            )

            // Antrean
            BottomNavTab(
                label = "Antrean",
                icon = if (currentTab == "Antrean") Icons.Default.QueuePlayNext else Icons.Outlined.QueuePlayNext,
                isSelected = currentTab == "Antrean",
                onClick = { onTabSelect("Antrean") }
            )

            // Riwayat
            BottomNavTab(
                label = "Riwayat",
                icon = if (currentTab == "Riwayat") Icons.Default.History else Icons.Outlined.History,
                isSelected = currentTab == "Riwayat",
                onClick = { onTabSelect("Riwayat") }
            )

            // Profil
            BottomNavTab(
                label = "Profil",
                icon = if (currentTab == "Profil") Icons.Default.Person else Icons.Outlined.Person,
                isSelected = currentTab == "Profil",
                onClick = { onTabSelect("Profil") }
            )
        }
    }
}

@Composable
private fun BottomNavTab(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFD1F4EC))
                    .padding(horizontal = 16.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = RTBrandTeal,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = RTBrandMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) RTBrandTeal else RTBrandMuted
        )
    }
}
