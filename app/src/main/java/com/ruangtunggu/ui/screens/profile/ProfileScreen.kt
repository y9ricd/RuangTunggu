package com.ruangtunggu.ui.screens.profile

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
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
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            ProfileBottomBar(
                onTabSelect = { tab ->
                    when (tab) {
                        "Beranda" -> navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                        "Antrean" -> navController.navigate(Routes.QueueMonitor("A-023"))
                        "Riwayat" -> navController.navigate(Routes.History)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        text = "Profil Saya",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kelola data pribadi Anda",
                        fontSize = 12.5.sp,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                }
            }

            // ── Profile Main Card ───────────────────────────────────────
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Avatar & Name Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(RTBrandTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "NP",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "Nama Pasien",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTBrandNavy
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Pasien Terdaftar",
                                    fontSize = 12.sp,
                                    color = RTBrandSlate
                                )
                            }
                        }

                        // Verified Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Terverifikasi",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = RTBrandBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Detail Items
                    ProfileInfoRow(label = "NIK", value = "3515020101950001")
                    Spacer(modifier = Modifier.height(14.dp))
                    ProfileInfoRow(label = "Tanggal Lahir", value = "01 Januari 1995")
                    Spacer(modifier = Modifier.height(14.dp))
                    ProfileInfoRow(label = "Jenis Kelamin", value = "Laki-laki")
                    Spacer(modifier = Modifier.height(14.dp))
                    ProfileInfoRow(label = "No. HP / WhatsApp", value = "081234567890")
                    Spacer(modifier = Modifier.height(14.dp))
                    ProfileInfoRow(label = "Email", value = "pasien@ruangtunggu.com")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Ubah Profil Action
                    Button(
                        onClick = { /* Ubah Profil */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                    ) {
                        Text(
                            text = "Ubah Profil",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Keluar Action
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Routes.Login) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFEF4444))
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keluar dari Akun",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = RTBrandSlate
        )
        Text(
            text = value,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = RTBrandNavy
        )
    }
}

@Composable
private fun ProfileBottomBar(
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
            ProfileTabItem(label = "Beranda", icon = Icons.Outlined.Home, isSelected = false, onClick = { onTabSelect("Beranda") })
            ProfileTabItem(label = "Antrean", icon = Icons.Outlined.QueuePlayNext, isSelected = false, onClick = { onTabSelect("Antrean") })
            ProfileTabItem(label = "Riwayat", icon = Icons.Outlined.History, isSelected = false, onClick = { onTabSelect("Riwayat") })
            ProfileTabItem(label = "Profil", icon = Icons.Default.Person, isSelected = true, onClick = { onTabSelect("Profil") })
        }
    }
}

@Composable
private fun ProfileTabItem(
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

