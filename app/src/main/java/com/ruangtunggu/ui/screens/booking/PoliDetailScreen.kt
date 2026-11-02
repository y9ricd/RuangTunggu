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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.ruangtunggu.ui.theme.RTBrandInputBg
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

data class PoliItem(
    val id: String,
    val name: String,
    val doctorInfo: String,
    val icon: ImageVector
)

@Composable
fun PoliDetailScreen(
    navController: NavController,
    hospitalId: String = "rs_01"
) {
    var selectedPoliId by remember { mutableStateOf("poli_anak") }

    val poliList = listOf(
        PoliItem("poli_umum", "Poli Umum", "8 dokter · mulai 08.00", Icons.Default.MedicalServices),
        PoliItem("poli_anak", "Poli Anak", "5 dokter · mulai 08.00", Icons.Default.ChildCare),
        PoliItem("poli_gigi", "Poli Gigi", "3 dokter · mulai 09.00", Icons.Default.Healing),
        PoliItem("poli_kandungan", "Poli Kandungan", "4 dokter · mulai 09.00", Icons.Default.PregnantWoman),
        PoliItem("poli_mata", "Poli Mata", "2 dokter · mulai 10.00", Icons.Default.Visibility),
        PoliItem("poli_penyakit_dalam", "Poli Penyakit Dalam", "6 dokter · mulai 08.00", Icons.Default.LocalHospital)
    )

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 16.dp, spotColor = Color(0x1A000000))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.DoctorList(selectedPoliId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                ) {
                    Text(
                        text = "Lanjut Pilih Dokter",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
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
                                text = "Pilih Poli",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Langkah 2 dari 3",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }
                }
            }

            // ── Hospital Summary Box ────────────────────────────────────
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
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE6F7F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = RTBrandTeal,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "RS Sehat Sidoarjo",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTBrandNavy
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "4.8 · 1,2 km · Buka 24 jam",
                                    fontSize = 11.5.sp,
                                    color = RTBrandSlate
                                )
                            }
                        }
                    }

                    Text(
                        text = "Ganti",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandTeal,
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .padding(6.dp)
                    )
                }
            }

            // ── Poli List ───────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Poli tersedia (6 poli)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    poliList.forEach { poli ->
                        val isSelected = selectedPoliId == poli.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFFF0FDFA) else RTBrandInputBg)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) RTBrandTeal else RTBrandBorder,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedPoliId = poli.id }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) RTBrandTeal else Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = poli.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else RTBrandTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = poli.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RTBrandNavy
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = poli.doctorInfo,
                                        fontSize = 11.5.sp,
                                        color = RTBrandSlate
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(RTBrandTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

