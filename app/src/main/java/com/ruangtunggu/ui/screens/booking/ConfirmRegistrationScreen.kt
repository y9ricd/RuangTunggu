package com.ruangtunggu.ui.screens.booking

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
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
fun ConfirmRegistrationScreen(
    navController: NavController,
    scheduleId: String = ""
) {
    Scaffold(
        containerColor = Color.White
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
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
                                text = "Ringkasan Pendaftaran",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Periksa kembali data Anda",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }
                }
            }

            // ── Main Confirmation Card ──────────────────────────────────
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
                    // Hospital Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE6F7F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = RTBrandTeal,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "RS Sehat Sidoarjo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTBrandNavy
                            )
                            Text(
                                text = "Jl. Raya Candi No. 45, Sidoarjo",
                                fontSize = 12.sp,
                                color = RTBrandSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = RTBrandBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Booking Details
                    SummaryRow(label = "Poli", value = "Poli Anak")
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRow(label = "Dokter", value = "dr. Abdul, Sp.A")
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRow(label = "Tanggal", value = "Kamis, 22 Okt 2026")
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRow(label = "Jam Praktik", value = "08:00 WIB")

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = RTBrandBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Patient Details
                    Text(
                        text = "Data Pasien",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(RTBrandInputBg)
                            .border(1.dp, RTBrandBorder, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(RTBrandTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Nama Pasien",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTBrandNavy
                            )
                            Text(
                                text = "NIK 3515••••••••••••",
                                fontSize = 12.sp,
                                color = RTBrandSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quota Info Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RTBrandTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kuota tersisa 12 slot untuk jadwal ini.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RTBrandTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dengan mengonfirmasi, Anda menyetujui ketentuan layanan dan tata tertib rumah sakit.",
                        fontSize = 11.sp,
                        color = RTBrandMuted,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Buttons: Batal & Konfirmasi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(RTBrandBorder)
                            )
                        ) {
                            Text(
                                text = "Batal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RTBrandNavy
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate(Routes.RegistrationSuccess) {
                                    popUpTo(Routes.Home) { inclusive = false }
                                }
                            },
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                        ) {
                            Text(
                                text = "Konfirmasi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
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
            fontWeight = FontWeight.Bold,
            color = RTBrandNavy
        )
    }
}

