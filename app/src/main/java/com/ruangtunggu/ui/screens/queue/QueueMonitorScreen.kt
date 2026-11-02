package com.ruangtunggu.ui.screens.queue

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTHeaderWave
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandInputBg
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

@Composable
fun QueueMonitorScreen(
    navController: NavController,
    registrationId: String = ""
) {
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            QueueBottomBar(
                onTabSelect = { tab ->
                    when (tab) {
                        "Beranda" -> navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
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
            // ── 1. Top Teal Header with Back Button & Title ─────────────
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Back Button
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.22f))
                                    .clickable { navController.popBackStack() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Pantau Antrean",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Diperbarui 1 menit lalu",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                            }
                        }

                        // More Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // ── 2. Primary Status Card: Circular Gauge + Serving vs Yours ─
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0x1F000000),
                        ambientColor = Color(0x12000000)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sisa Antrean",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandTeal
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Circular Segment Gauge
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            // Background ring
                            drawCircle(
                                color = Color(0xFFE2E8F0),
                                radius = size.minDimension / 2 - strokeWidth / 2,
                                style = Stroke(width = strokeWidth)
                            )
                            // Active progress arc
                            drawArc(
                                color = RTBrandTeal,
                                startAngle = -90f,
                                sweepAngle = 260f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Text(
                            text = "5",
                            fontSize = 46.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RTBrandTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Estimasi 25 menit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandTeal
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Two Comparison Boxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Sedang dilayani
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(RTBrandInputBg)
                                .border(1.dp, RTBrandBorder, RoundedCornerShape(16.dp))
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Sedang dilayani",
                                    fontSize = 12.sp,
                                    color = RTBrandSlate
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "A-018",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTBrandNavy
                                )
                            }
                        }

                        // Nomor Anda
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF0FDFA))
                                .border(1.5.dp, RTBrandTeal, RoundedCornerShape(16.dp))
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Nomor Anda",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RTBrandTeal
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "A-023",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTBrandTeal
                                )
                            }
                        }
                    }
                }
            }

            // ── 3. Urutan Berikutnya Card ───────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-16).dp)
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = Color(0x14000000)
                    )
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .padding(18.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Urutan berikutnya",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pills Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("A-019", "A-020", "A-021", "A-022").forEach { num ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, RTBrandBorder, RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = num,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RTBrandNavy
                                )
                            }
                        }

                        // Patient's Ticket (Highlighted in Teal)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(RTBrandTeal)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "A-023",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notification banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = RTBrandTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notifikasi dikirim saat sisa antrean ≤ 2",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RTBrandTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── 4. Refresh Status Button ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                OutlinedButton(
                    onClick = { /* trigger refresh */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = RTBrandTeal
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(RTBrandTeal)
                    )
                ) {
                    Text(
                        text = "Refresh status",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/**
 * Bottom Nav for Queue Screen with Antrean tab selected
 */
@Composable
private fun QueueBottomBar(
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
            QueueTab(
                label = "Beranda",
                icon = Icons.Outlined.Home,
                isSelected = false,
                onClick = { onTabSelect("Beranda") }
            )

            QueueTab(
                label = "Antrean",
                icon = Icons.Default.QueuePlayNext,
                isSelected = true,
                onClick = { onTabSelect("Antrean") }
            )

            QueueTab(
                label = "Riwayat",
                icon = Icons.Outlined.History,
                isSelected = false,
                onClick = { onTabSelect("Riwayat") }
            )

            QueueTab(
                label = "Profil",
                icon = Icons.Outlined.Person,
                isSelected = false,
                onClick = { onTabSelect("Profil") }
            )
        }
    }
}

@Composable
private fun QueueTab(
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

