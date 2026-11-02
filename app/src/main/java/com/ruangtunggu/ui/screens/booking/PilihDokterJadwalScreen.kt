package com.ruangtunggu.ui.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.domain.model.DoctorScheduleItem
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTShimmerCardPlaceholder
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun PilihDokterJadwalScreen(
    hospitalName: String,
    poliName: String,
    poliShortName: String,
    onBackClick: () -> Unit,
    onContinueToConfirm: (DoctorScheduleItem, String, String) -> Unit,
    viewModel: PilihDokterJadwalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(poliShortName) {
        viewModel.loadDoctors(poliShortName)
    }

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(
                color = RTColors.PureWhite,
                shadowElevation = 16.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    val selectedDoc = uiState.selectedDoctor
                    val selectedSlot = uiState.selectedSlotTime
                    val dateLabel = uiState.dates.getOrNull(uiState.selectedDateIndex)?.fullLabel ?: "Kam, 22 Okt"
                    RTPrimaryButton(
                        text = "Pilih dokter & jadwal",
                        enabled = selectedDoc != null && selectedSlot != null && !uiState.isLoading,
                        onClick = {
                            if (selectedDoc != null && selectedSlot != null) {
                                onContinueToConfirm(selectedDoc, dateLabel, selectedSlot)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            RTCurvedHeader(
                height = 168.dp,
                bottomCornerRadius = 32.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Pilih Dokter & Jadwal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$poliName · $hospitalName",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-36).dp)
                    .padding(horizontal = 20.dp)
            ) {
                RTFloatingCard(
                    cornerRadius = 20.dp,
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.dates.forEachIndexed { idx, day ->
                            val isSelected = idx == uiState.selectedDateIndex
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(62.dp)
                                    .springClickable { viewModel.onDateSelected(idx) }
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) RTColors.BrandPrimaryTeal else RTColors.InputBackground
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = day.dayShort,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) RTColors.PureWhite.copy(alpha = 0.9f) else RTColors.SlateBodyText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = day.dayNumber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) RTColors.PureWhite else RTColors.NavyHeading
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                val currentDayLabel = uiState.dates.getOrNull(uiState.selectedDateIndex)?.fullLabel ?: "Kamis, 22 Okt"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.doctors.size} dokter tersedia",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    Text(
                        text = currentDayLabel,
                        fontSize = 12.5.sp,
                        color = RTColors.SlateBodyText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.isLoading) {
                    repeat(3) {
                        RTShimmerCardPlaceholder(height = 148.dp, cornerRadius = 20.dp)
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                } else {
                    uiState.doctors.forEach { doctor ->
                        val isDocSelected = doctor.id == uiState.selectedDoctor?.id
                        val showSmartSuggestion = doctor.id == uiState.fullSlotAlertDoctorId ||
                            doctor.slots.any { it.isFull }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isDocSelected) 1.8.dp else 1.dp,
                                    color = if (isDocSelected) RTColors.BrandPrimaryTeal else RTColors.PureWhite,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            shape = RoundedCornerShape(20.dp),
                            color = RTColors.PureWhite,
                            shadowElevation = if (isDocSelected) 10.dp else 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(RTColors.MintSurfaceActive),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = doctor.initials,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = RTColors.BrandPrimaryTeal
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = doctor.name,
                                            fontSize = 15.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RTColors.NavyHeading
                                        )
                                        Text(
                                            text = doctor.specialty,
                                            fontSize = 12.5.sp,
                                            color = RTColors.SlateBodyText
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = RTColors.BrandPrimaryTeal,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${doctor.rating} · ${doctor.experienceYears} tahun pengalaman",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = RTColors.BrandPrimaryTeal
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = RTColors.InputBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Pilih jam",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    doctor.slots.forEach { slot ->
                                        val isSlotSelected = isDocSelected && uiState.selectedSlotTime == slot.time && !slot.isFull
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(38.dp)
                                                .springClickable {
                                                    viewModel.onSlotClicked(doctor, slot.time, slot.isFull)
                                                }
                                                .clip(RoundedCornerShape(19.dp))
                                                .background(
                                                    when {
                                                        slot.isFull -> Color(0xFFF1F5F9)
                                                        isSlotSelected -> RTColors.BrandPrimaryTeal
                                                        else -> RTColors.PureWhite
                                                    }
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = when {
                                                        slot.isFull -> RTColors.InputBorder
                                                        isSlotSelected -> RTColors.BrandPrimaryTeal
                                                        else -> RTColors.InputBorder
                                                    },
                                                    shape = RoundedCornerShape(19.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (slot.isFull) "${slot.time} (Penuh)" else slot.time,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = if (slot.isFull) TextDecoration.LineThrough else TextDecoration.None,
                                                color = when {
                                                    slot.isFull -> RTColors.MutedSlate
                                                    isSlotSelected -> RTColors.PureWhite
                                                    else -> RTColors.NavyHeading
                                                }
                                            )
                                        }
                                    }
                                }

                                if (showSmartSuggestion && uiState.selectedDateIndex == 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(RTColors.QuickActionUniformBg)
                                            .border(1.dp, RTColors.MintSurfaceActive, RoundedCornerShape(12.dp))
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = RTColors.BrandPrimaryTeal,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Saran Pintar (Kuota Penuh 30/30):",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RTColors.BrandDarkTeal
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .springClickable {
                                                        viewModel.onApplyTomorrowSmartSuggestion(doctor.id)
                                                    }
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(RTColors.BrandPrimaryTeal)
                                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Jadwal Besok (09:00)",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = RTColors.PureWhite
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .springClickable {
                                                        viewModel.onSelectAlternativeDoctorInSamePoli(doctor.id)
                                                    }
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(RTColors.PureWhite)
                                                    .border(1.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Lihat Dokter Lain",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = RTColors.BrandPrimaryTeal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}
