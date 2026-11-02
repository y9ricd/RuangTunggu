package com.ruangtunggu.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTHeaderWave
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandGreenBadge
import com.ruangtunggu.ui.theme.RTBrandGreenText
import com.ruangtunggu.ui.theme.RTBrandInputBg
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

@Composable
fun HistoryScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf("Aktif") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            HistoryBottomBar(
                onTabSelect = { tab ->
                    when (tab) {
                        "Beranda" -> navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                        "Antrean" -> navController.navigate(Routes.QueueMonitor("A-023"))
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
            // ── Top Wave Header ─────────────────────────────────────────
            RTHeaderWave(
                modifier = Modifier.fillMaxWidth(),
                height = 200.dp,
                waveDip = 0.86f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Riwayat Antrean",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Semua pendaftaran antrean Anda",
                        fontSize = 12.5.sp,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                }
            }

            // ── Tab Bar (Aktif, Selesai, Batal) ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color(0x1F000000)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Aktif (2)", "Selesai (4)", "Batal (2)").forEach { tabName ->
                        val tabKey = tabName.substringBefore(" ")
                        val isSelected = selectedTab == tabKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) RTBrandTeal else Color.Transparent)
                                .clickable { selectedTab = tabKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabName,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else RTBrandSlate
                            )
                        }
                    }
                }
            }

            // ── Tab Content ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (selectedTab) {
                    "Aktif" -> {
                        // Ticket 1: Active
                        ActiveHistoryCard(
                            hospitalName = "RS Sehat Sidoarjo",
                            poliName = "Poli Anak · dr. Abdul, Sp.A",
                            dateTime = "Kam, 22 Okt · 09:00",
                            queueNumber = "A-023",
                            statusText = "Sisa 5 antrean · Est. 25 menit",
                            badgeColor = RTBrandGreenBadge,
                            badgeTextColor = RTBrandGreenText,
                            badgeLabel = "Aktif",
                            onPantauClick = { navController.navigate(Routes.QueueMonitor("A-023")) },
                            onCancelClick = {}
                        )

                        // Ticket 2: Paused
                        ActiveHistoryCard(
                            hospitalName = "RS Harapan Bunda",
                            poliName = "Poli Kandungan · dr. Clara Wijaya",
                            dateTime = "Kam, 22 Okt · 10:30",
                            queueNumber = "C-012",
                            statusText = "Dokter berhalangan · Lihat info",
                            badgeColor = Color(0xFFFEF3C7),
                            badgeTextColor = Color(0xFFD97706),
                            badgeLabel = "Dijeda",
                            onPantauClick = { navController.navigate(Routes.QueueMonitor("C-012")) },
                            onCancelClick = {}
                        )
                    }

                    "Selesai" -> {
                        CompletedHistoryCard(
                            hospitalName = "RS Sehat Sidoarjo",
                            poliName = "Poli Umum · dr. Rian Pratama",
                            dateTime = "Sen, 12 Okt · 08:30",
                            queueNumber = "U-007"
                        )
                        CompletedHistoryCard(
                            hospitalName = "RS Harapan Bunda",
                            poliName = "Poli Gigi · drg. Maya",
                            dateTime = "Rab, 30 Sep · 14:00",
                            queueNumber = "G-015"
                        )
                    }

                    "Batal" -> {
                        CancelledHistoryCard(
                            hospitalName = "RSUD Sidoarjo",
                            poliName = "Poli Mata · dr. Hendra",
                            dateTime = "Kam, 15 Okt · 11:00",
                            reason = "Dibatalkan oleh pasien"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ActiveHistoryCard(
    hospitalName: String,
    poliName: String,
    dateTime: String,
    queueNumber: String,
    statusText: String,
    badgeColor: Color,
    badgeTextColor: Color,
    badgeLabel: String,
    onPantauClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0x1F000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, RTBrandBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = hospitalName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )
                    Text(
                        text = "$poliName · $dateTime",
                        fontSize = 12.sp,
                        color = RTBrandSlate
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = RTBrandBorder)
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = queueNumber,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RTBrandTeal
                    )
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        color = RTBrandSlate
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onPantauClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = "Pantau", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = onCancelClick,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(36.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(RTBrandBorder)
                        )
                    ) {
                        Text(text = "Batal", fontSize = 12.sp, color = RTBrandSlate)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompletedHistoryCard(
    hospitalName: String,
    poliName: String,
    dateTime: String,
    queueNumber: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, RTBrandBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = hospitalName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RTBrandNavy)
                Text(text = "$poliName · $dateTime", fontSize = 12.sp, color = RTBrandSlate)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Nomor: $queueNumber", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = RTBrandTeal)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE6F7F5))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "Selesai", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RTBrandTeal)
            }
        }
    }
}

@Composable
private fun CancelledHistoryCard(
    hospitalName: String,
    poliName: String,
    dateTime: String,
    reason: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, RTBrandBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = hospitalName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RTBrandNavy)
                Text(text = "$poliName · $dateTime", fontSize = 12.sp, color = RTBrandSlate)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = reason, fontSize = 11.sp, color = Color(0xFFEF4444))
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEE2E2))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "Batal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
            }
        }
    }
}

@Composable
private fun HistoryBottomBar(
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
            HistoryTabItem(label = "Beranda", icon = Icons.Outlined.Home, isSelected = false, onClick = { onTabSelect("Beranda") })
            HistoryTabItem(label = "Antrean", icon = Icons.Outlined.QueuePlayNext, isSelected = false, onClick = { onTabSelect("Antrean") })
            HistoryTabItem(label = "Riwayat", icon = Icons.Default.History, isSelected = true, onClick = { onTabSelect("Riwayat") })
            HistoryTabItem(label = "Profil", icon = Icons.Outlined.Person, isSelected = false, onClick = { onTabSelect("Profil") })
        }
    }
}

@Composable
private fun HistoryTabItem(
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
                Icon(imageVector = icon, contentDescription = label, tint = RTBrandTeal, modifier = Modifier.size(20.dp))
            }
        } else {
            Icon(imageVector = icon, contentDescription = label, tint = RTBrandMuted, modifier = Modifier.size(22.dp))
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
