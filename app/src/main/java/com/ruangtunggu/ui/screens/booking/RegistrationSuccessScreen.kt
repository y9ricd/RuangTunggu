package com.ruangtunggu.ui.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

@Composable
fun RegistrationSuccessScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = Color.White
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
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Pendaftaran Berhasil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Simpan nomor antrean Anda",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                }
            }

            // ── Ticket Success Card ─────────────────────────────────────
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
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Checkmark Badge
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Berhasil",
                            tint = Color(0xFF15803D),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Nomor Antrean Anda",
                        fontSize = 13.sp,
                        color = RTBrandSlate
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "A-023",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RTBrandTeal,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Estimasi dilayani: 08:30 WIB",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandNavy
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = RTBrandBorder)
                    Spacer(modifier = Modifier.height(18.dp))

                    // Detail rows
                    TicketRow(label = "Rumah Sakit", value = "RS Sehat Sidoarjo")
                    Spacer(modifier = Modifier.height(12.dp))
                    TicketRow(label = "Poli", value = "Poli Anak")
                    Spacer(modifier = Modifier.height(12.dp))
                    TicketRow(label = "Dokter", value = "dr. Abdul, Sp.A")
                    Spacer(modifier = Modifier.height(12.dp))
                    TicketRow(label = "Jadwal", value = "Kam, 22 Okt · 08:00")

                    Spacer(modifier = Modifier.height(26.dp))

                    // Primary Action: Pantau Antrean
                    Button(
                        onClick = {
                            navController.navigate(Routes.QueueMonitor("A-023"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                    ) {
                        Text(
                            text = "Pantau Antrean",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Action: Kembali ke Beranda
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Routes.Home) {
                                popUpTo(Routes.Home) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(RTBrandBorder)
                        )
                    ) {
                        Text(
                            text = "Kembali ke Beranda",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RTBrandNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TicketRow(label: String, value: String) {
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
