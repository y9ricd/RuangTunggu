package com.ruangtunggu.ui.screens.booking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.R
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTQrCodeCanvas
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun KonfirmasiPendaftaranScreen(
    hospitalId: String,
    hospitalName: String,
    poliId: String,
    poliName: String,
    poliShortName: String,
    doctorId: String,
    doctorName: String,
    dateLabel: String,
    slotTime: String,
    onBackClick: () -> Unit,
    onBookingConfirmed: () -> Unit,
    viewModel: KonfirmasiPendaftaranViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPreview = LocalInspectionMode.current

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(
                color = RTColors.PureWhite,
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.BrandPrimaryTeal),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RTColors.BrandPrimaryTeal),
                        modifier = Modifier
                            .weight(0.38f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Batal",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }

                    Box(modifier = Modifier.weight(0.62f)) {
                        if (uiState.isSlotFullPreview) {
                            RTPrimaryButton(
                                text = "Pilih jadwal lain",
                                onClick = onBackClick
                            )
                        } else {
                            RTPrimaryButton(
                                text = "Konfirmasi pendaftaran",
                                isLoading = uiState.isLoading,
                                onClick = {
                                    viewModel.confirmBooking(
                                        hospitalId = hospitalId,
                                        hospitalName = hospitalName,
                                        poliId = poliId,
                                        poliName = poliName,
                                        poliShortName = poliShortName,
                                        doctorId = doctorId,
                                        doctorName = doctorName,
                                        dateLabel = dateLabel,
                                        slotTime = slotTime,
                                        onSuccess = onBookingConfirmed
                                    )
                                }
                            )
                        }
                    }
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ringkasan Pendaftaran",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Periksa kembali data Anda",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .springClickable { viewModel.toggleSlotFullPreview() }
                            .clip(RoundedCornerShape(50))
                            .background(RTColors.PureWhite.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (uiState.isSlotFullPreview) "Mode: Penuh" else "Cek Penuh",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
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
                    cornerRadius = 22.dp,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(RTColors.QuickActionUniformBg)
                        ) {
                            if (!isPreview) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_rs_sehat_sidoarjo),
                                    contentDescription = hospitalName,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = hospitalName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Text(
                                text = "Jl. Contoh No. 1, Sidoarjo",
                                fontSize = 12.5.sp,
                                color = RTColors.SlateBodyText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = RTColors.InputBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    SummaryRowItem("Poli", poliShortName)
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRowItem("Dokter", doctorName)
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRowItem("Tanggal", "$dateLabel 2026")
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jam",
                            fontSize = 13.5.sp,
                            color = RTColors.MutedSlate
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$slotTime WIB",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            if (uiState.isSlotFullPreview) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(RTColors.ErrorSurface)
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "Penuh",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RTColors.ErrorRed
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = RTColors.InputBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(RTColors.MintSurfaceActive),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.patientProfile.initials,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = uiState.patientProfile.fullName,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Text(
                                text = "NIK ${uiState.patientProfile.maskedNik}",
                                fontSize = 12.sp,
                                color = RTColors.SlateBodyText
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(RTColors.MintSurfaceActive)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Pasien",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (uiState.isSlotFullPreview) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(RTColors.ErrorSurface)
                            .border(1.dp, RTColors.ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RTColors.ErrorRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Slot penuh",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.ErrorRed
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Kuota jadwal ini sudah habis.\nSilakan pilih jadwal lain.",
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = RTColors.ErrorRed
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(RTColors.QuickActionUniformBg)
                            .border(1.dp, RTColors.MintSurfaceActive, RoundedCornerShape(18.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RTColors.BrandPrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Kuota tersedia",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.BrandDarkTeal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Kuota tersisa ${uiState.remainingQuotaSlots} slot untuk jadwal ini.\nKonfirmasi sekarang agar tidak terlewat.",
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dengan mengonfirmasi, Anda menyetujui ketentuan layanan.",
                        fontSize = 11.5.sp,
                        color = RTColors.MutedSlate,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun SummaryRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.5.sp,
            color = RTColors.MutedSlate
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading
        )
    }
}

@Suppress("FunctionName")
@Composable
fun PendaftaranBerhasilScreen(
    onPantauAntreanClick: () -> Unit,
    onBackToBerandaClick: () -> Unit,
    viewModel: KonfirmasiPendaftaranViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ticket = uiState.bookedQueue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RTCurvedHeader(
            height = 260.dp,
            bottomCornerRadius = 36.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 22.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(RTColors.PureWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Berhasil",
                        tint = RTColors.BrandPrimaryTeal,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pendaftaran berhasil",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Simpan nomor antrean Anda",
                    fontSize = 13.sp,
                    color = RTColors.PureWhite.copy(alpha = 0.92f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-44).dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RTFloatingCard(
                cornerRadius = 24.dp,
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nomor antrean",
                        fontSize = 13.sp,
                        color = RTColors.SlateBodyText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ticket?.userQueueNumber ?: "A-023",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RTColors.NavyHeading,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(RTColors.MintSurfaceActive)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Estimasi dilayani ${ticket?.estimatedServeClock ?: "08:30"}",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                    ) {
                        drawLine(
                            color = RTColors.ErrorRed.copy(alpha = 0.65f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Rumah sakit", fontSize = 11.5.sp, color = RTColors.SlateBodyText)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ticket?.hospitalName ?: "RS Sehat Sidoarjo",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                        }
                        Column(modifier = Modifier.weight(0.7f)) {
                            Text("Poli", fontSize = 11.5.sp, color = RTColors.SlateBodyText)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ticket?.poliName?.removePrefix("Poli ") ?: "Anak",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Dokter", fontSize = 11.5.sp, color = RTColors.SlateBodyText)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ticket?.doctorName ?: "dr. Abdul, Sp.A",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                        }
                        Column(modifier = Modifier.weight(0.7f)) {
                            Text("Jadwal", fontSize = 11.5.sp, color = RTColors.SlateBodyText)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ticket?.scheduleDateTime ?: "Kam, 22 Okt · 08:00",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .size(142.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(RTColors.PureWhite)
                            .border(1.dp, RTColors.InputBorder, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RTQrCodeCanvas(
                            qrPayload = ticket?.qrPayload ?: "RT-QR|A-023",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "✓ Tersimpan otomatis untuk mode tanpa sinyal (Offline-First)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = RTColors.BrandPrimaryTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            RTPrimaryButton(
                text = "Pantau antrean",
                onClick = onPantauAntreanClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Kembali ke beranda",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.BrandPrimaryTeal,
                modifier = Modifier
                    .springClickable(onClick = onBackToBerandaClick)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
    }
}
