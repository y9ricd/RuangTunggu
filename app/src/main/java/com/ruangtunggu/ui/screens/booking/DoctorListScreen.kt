package com.ruangtunggu.ui.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Star
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

data class DoctorItem(
    val id: String,
    val initials: String,
    val name: String,
    val specialty: String,
    val rating: String,
    val experience: String,
    val slots: List<String>
)

data class DayItem(
    val dayName: String,
    val dateNumber: String,
    val fullDate: String
)

@Composable
fun DoctorListScreen(
    navController: NavController,
    poliId: String = "poli_anak"
) {
    var selectedDateIndex by remember { mutableStateOf(0) }
    var selectedSlot by remember { mutableStateOf<Pair<String, String>?>(null) }

    val days = listOf(
        DayItem("Kam", "22", "Kamis, 22 Okt 2026"),
        DayItem("Jum", "23", "Jumat, 23 Okt 2026"),
        DayItem("Sab", "24", "Sabtu, 24 Okt 2026"),
        DayItem("Min", "25", "Minggu, 25 Okt 2026"),
        DayItem("Sen", "26", "Senin, 26 Okt 2026")
    )

    val doctors = listOf(
        DoctorItem(
            id = "doc_01",
            initials = "AL",
            name = "dr. Abdul, Sp.A",
            specialty = "Spesialis Anak",
            rating = "4.9",
            experience = "6 tahun pengalaman",
            slots = listOf("08:00", "09:30", "13:00")
        ),
        DoctorItem(
            id = "doc_02",
            initials = "BS",
            name = "dr. Budi Santoso, Sp.A",
            specialty = "Spesialis Anak",
            rating = "4.8",
            experience = "10 tahun pengalaman",
            slots = listOf("10:00", "14:00", "15:30")
        ),
        DoctorItem(
            id = "doc_03",
            initials = "CW",
            name = "dr. Clara Wijaya, Sp.A",
            specialty = "Spesialis Anak",
            rating = "4.9",
            experience = "8 tahun pengalaman",
            slots = listOf("08:30", "11:00")
        )
    )

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
                                text = "Pilih Dokter & Jadwal",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Poli Anak · RS Sehat Sidoarjo",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }
                }
            }

            // ── Horizontal Date Selector ────────────────────────────────
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
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEachIndexed { index, day ->
                        val isSelected = selectedDateIndex == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) RTBrandTeal else Color.Transparent)
                                .clickable { selectedDateIndex = index }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = day.dayName,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.85f) else RTBrandSlate
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = day.dateNumber,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else RTBrandNavy
                            )
                        }
                    }
                }
            }

            // ── Doctors List ────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "${doctors.size} dokter tersedia · ${days[selectedDateIndex].dayName}, ${days[selectedDateIndex].dateNumber} Okt",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandNavy
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    doctors.forEach { doctor ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    spotColor = Color(0x14000000)
                                )
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(1.dp, RTBrandBorder, RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Avatar initials
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE6F7F5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = doctor.initials,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RTBrandTeal
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = doctor.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RTBrandNavy
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${doctor.specialty} · ${doctor.experience}",
                                            fontSize = 11.5.sp,
                                            color = RTBrandSlate
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
                                                text = doctor.rating,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = RTBrandNavy
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Pilih jam praktik",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RTBrandSlate
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    doctor.slots.forEach { slot ->
                                        val isSlotSelected = selectedSlot == (doctor.id to slot)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSlotSelected) RTBrandTeal else RTBrandInputBg)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSlotSelected) RTBrandTeal else RTBrandBorder,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable {
                                                    selectedSlot = doctor.id to slot
                                                    // Navigate to Confirm Registration with params
                                                    navController.navigate(
                                                        Routes.ConfirmRegistration(
                                                            scheduleId = "${doctor.id}_${slot.replace(":", "")}"
                                                        )
                                                    )
                                                }
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = slot,
                                                fontSize = 12.5.sp,
                                                fontWeight = if (isSlotSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSlotSelected) Color.White else RTBrandNavy
                                            )
                                        }
                                    }
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
