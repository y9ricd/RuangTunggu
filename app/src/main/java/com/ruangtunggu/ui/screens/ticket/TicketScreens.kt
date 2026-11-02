package com.ruangtunggu.ui.screens.ticket

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTQrCodeCanvas
import com.ruangtunggu.ui.components.tactilePressScale
import com.ruangtunggu.ui.theme.RTColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TicketQrUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ticket: ActiveQueue = ActiveQueue(),
    val profile: PatientProfile = PatientProfile()
)

@HiltViewModel
class TicketQrViewModel @Inject constructor(
    queueRepository: QueueRepository,
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TicketQrUiState(
            ticket = queueRepository.getOfflineCachedTicket() ?: ActiveQueue(),
            profile = authRepository.currentProfileFlow.value
        )
    )
    val uiState: StateFlow<TicketQrUiState> = _uiState.asStateFlow()
}

@Suppress("FunctionName")
@Composable
fun TiketQrScreen(
    onBackClick: () -> Unit,
    viewModel: TicketQrViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ticket = uiState.ticket
    val isPaused = ticket.phase == QueuePhase.DIJEDA

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.BrandPrimaryTeal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .tactilePressScale()
                    ) {
                        Text(
                            text = "Tutup",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
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
            RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
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
                            text = "Tiket Antrean",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tunjukkan ke petugas loket (Tersedia Offline)",
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
                    cornerRadius = 24.dp,
                    contentPadding = PaddingValues(22.dp)
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

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = ticket.userQueueNumber,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RTColors.BrandPrimaryTeal
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (isPaused) RTColors.PausedBadgeBg else RTColors.ActiveBadgeBg
                                )
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = when (ticket.phase) {
                                    QueuePhase.DIJEDA -> "Dijeda"
                                    QueuePhase.DIPANGGIL -> "Dipanggil ke Loket"
                                    else -> "Aktif · Offline Ready"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPaused) RTColors.WarningAmberText else RTColors.ActiveBadgeText
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = RTColors.InputBorder)
                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .size(196.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(RTColors.PureWhite)
                                .border(1.dp, RTColors.InputBorder, RoundedCornerShape(18.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            RTQrCodeCanvas(
                                qrPayload = ticket.qrPayload,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Pindai QR di loket pendaftaran",
                            fontSize = 12.5.sp,
                            color = RTColors.SlateBodyText
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = RTColors.InputBorder)
                        Spacer(modifier = Modifier.height(14.dp))

                        TicketMetaRow("Pasien", uiState.profile.fullName)
                        Spacer(modifier = Modifier.height(10.dp))
                        TicketMetaRow(
                            "Poli · Dokter",
                            "${ticket.poliName.removePrefix("Poli ")} · ${ticket.doctorName}"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TicketMetaRow("Jadwal", ticket.scheduleDateTime)

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(RTColors.QuickActionUniformBg)
                                .border(1.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = RTColors.BrandPrimaryTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Naikkan kecerahan layar agar QR mudah dipindai",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun TicketMetaRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = RTColors.MutedSlate)
        Text(
            text = value,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading
        )
    }
}

@Suppress("FunctionName")
@Composable
fun DatangKeLoketScreen(
    onBackClick: () -> Unit,
    onShowQrClick: () -> Unit,
    viewModel: TicketQrViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ticket = uiState.ticket

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            Surface(color = RTColors.PureWhite, shadowElevation = 16.dp) {
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
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Petunjuk arah", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        RTPrimaryButton(
                            text = "Tunjukkan tiket",
                            onClick = onShowQrClick
                        )
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
            RTCurvedHeader(height = 168.dp, bottomCornerRadius = 32.dp) {
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
                            text = "Datang ke Loket",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ticket.hospitalName,
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
                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RTColors.MintSurfaceActive),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AddBox,
                                contentDescription = null,
                                tint = RTColors.BrandPrimaryTeal,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Loket Pendaftaran",
                                fontSize = 15.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Text(
                                text = "Gedung A · Lantai 1 · dekat pintu utama",
                                fontSize = 12.sp,
                                color = RTColors.SlateBodyText
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(RTColors.ActiveBadgeBg)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Buka",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.ActiveBadgeText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = RTColors.InputBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = RTColors.SlateBodyText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Senin–Sabtu · 08.00–16.00 WIB",
                            fontSize = 13.sp,
                            color = RTColors.SlateBodyText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(18.dp)) {
                    Text(
                        text = "Langkah menuju loket",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StepRowItem("1", "Masuk lewat pintu utama Gedung A")
                    Spacer(modifier = Modifier.height(12.dp))
                    StepRowItem("2", "Lurus ±20 m menuju Loket Pendaftaran")
                    Spacer(modifier = Modifier.height(12.dp))
                    StepRowItem("3", "Tunjukkan tiket ${ticket.userQueueNumber} dan KTP ke petugas")
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Yang perlu dibawa",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.SlateBodyText
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("KTP", "Tiket / QR ${ticket.userQueueNumber}", "Kartu berobat").forEachIndexed { idx, tag ->
                        val active = idx == 0
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (active) RTColors.BrandPrimaryTeal else RTColors.PureWhite)
                                .border(
                                    1.dp,
                                    if (active) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                    RoundedCornerShape(18.dp)
                                )
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (active) RTColors.PureWhite else RTColors.NavyHeading
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun StepRowItem(number: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(RTColors.BrandPrimaryTeal),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.PureWhite
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 13.5.sp,
            color = RTColors.NavyHeading
        )
    }
}
