package com.ruangtunggu.ui.screens.beranda

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.R
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.QueueHistoryItem
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.domain.model.QueueStatus
import com.ruangtunggu.ui.components.BatalkanAntreanBottomSheet
import com.ruangtunggu.ui.components.HomeTab
import com.ruangtunggu.ui.components.RTBerandaEmptyQueueCard
import com.ruangtunggu.ui.components.RTBottomNavigationBar
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTEmptyStateView
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPostConsultationReviewCard
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTQueueCard
import com.ruangtunggu.ui.components.RTShimmerCardPlaceholder
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.components.tactilePressScale
import com.ruangtunggu.ui.theme.RTColors
import kotlinx.coroutines.delay

@Suppress("FunctionName")
@Composable
fun BerandaScreen(
    initialTab: String = "BERANDA",
    onNavigateToCariRs: (String) -> Unit,
    onNavigateToPilihPoli: (HospitalItem) -> Unit,
    onNavigateToTiketQr: () -> Unit,
    onNavigateToDatangKeLoket: () -> Unit,
    onNavigateToChatPetugas: () -> Unit,
    onNavigateToNotifikasi: () -> Unit,
    onNavigateToAturPengingat: () -> Unit,
    onNavigateToBeriPenilaian: () -> Unit,
    onNavigateToReschedule: () -> Unit,
    onNavigateToUbahProfil: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: BerandaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialTab) {
        viewModel.setInitialTab(initialTab)
    }

    // Hardware/System Back Button Support when on Antrean, Riwayat, or Profil tabs
    BackHandler(enabled = uiState.selectedTab != HomeTab.BERANDA) {
        viewModel.onTabSelected(HomeTab.BERANDA)
    }

    val activeQueueForCancel = uiState.activeQueue
    if (uiState.showCancelBottomSheet && activeQueueForCancel != null) {
        BatalkanAntreanBottomSheet(
            queue = activeQueueForCancel,
            onDismiss = viewModel::onDismissCancelSheet,
            onConfirmCancel = viewModel::onConfirmCancelQueue
        )
    }

    Scaffold(
        containerColor = RTColors.ScreenBackground,
        bottomBar = {
            RTBottomNavigationBar(
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (uiState.selectedTab) {
                HomeTab.BERANDA -> BerandaDashboardTab(
                    uiState = uiState,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onPoliChipSelected = viewModel::onPoliChipSelected,
                    onDaftarAntreanClick = { onNavigateToCariRs(uiState.selectedPoliChip) },
                    onPantauClick = { viewModel.onTabSelected(HomeTab.ANTREAN) },
                    onProfileHeaderClick = { viewModel.onTabSelected(HomeTab.PROFIL) },
                    onShowQrTicketClick = onNavigateToTiketQr,
                    onAturPengingatClick = onNavigateToAturPengingat,
                    onChatPetugasClick = onNavigateToChatPetugas,
                    onNotifikasiClick = onNavigateToNotifikasi,
                    onBeriPenilaianClick = onNavigateToBeriPenilaian,
                    onDismissReviewClick = viewModel::onDismissReviewCard,
                    onHospitalCardClick = onNavigateToPilihPoli,
                    onQuickActionTab = viewModel::onTabSelected,
                    onSetQueuePhase = viewModel::onSetQueuePhase,
                    onCompleteQueueForReview = viewModel::onCompleteQueueForReview,
                    onOpenCancelSheet = viewModel::onOpenCancelSheet
                )
                HomeTab.ANTREAN -> PantauAntreanTab(
                    queue = uiState.activeQueue,
                    onBackClick = { viewModel.onTabSelected(HomeTab.BERANDA) },
                    onDaftarAntreanBaruClick = { onNavigateToCariRs("Semua") },
                    onRefreshClick = viewModel::onSimulateNextQueueStep,
                    onSetQueuePhase = viewModel::onSetQueuePhase,
                    onCompleteQueueClick = viewModel::onCompleteQueueForReview,
                    onShowQrTicketClick = onNavigateToTiketQr,
                    onDatangKeLoketClick = onNavigateToDatangKeLoket,
                    onChatPetugasClick = onNavigateToChatPetugas,
                    onRescheduleClick = onNavigateToReschedule,
                    onCancelQueueClick = viewModel::onOpenCancelSheet
                )
                HomeTab.RIWAYAT -> RiwayatAntreanTab(
                    historyList = uiState.queueHistory,
                    selectedFilter = uiState.selectedHistoryFilter,
                    onBackClick = { viewModel.onTabSelected(HomeTab.BERANDA) },
                    onFilterSelected = viewModel::onHistoryFilterSelected,
                    onPantauClick = { viewModel.onTabSelected(HomeTab.ANTREAN) },
                    onCancelClick = viewModel::onOpenCancelSheet,
                    onDaftarBaruClick = { onNavigateToCariRs("Semua") }
                )
                HomeTab.PROFIL -> ProfilSayaTab(
                    profile = uiState.patientProfile,
                    onBackClick = { viewModel.onTabSelected(HomeTab.BERANDA) },
                    onUbahProfilClick = onNavigateToUbahProfil,
                    onNotifikasiClick = onNavigateToNotifikasi,
                    onPengingatClick = onNavigateToAturPengingat,
                    onChatPetugasClick = onNavigateToChatPetugas,
                    onLogoutClick = { viewModel.onLogout(onLogoutClick) }
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun BerandaDashboardTab(
    uiState: BerandaUiState,
    onSearchQueryChanged: (String) -> Unit,
    onPoliChipSelected: (String) -> Unit,
    onDaftarAntreanClick: () -> Unit,
    onPantauClick: () -> Unit,
    onProfileHeaderClick: () -> Unit,
    onShowQrTicketClick: () -> Unit,
    onAturPengingatClick: () -> Unit,
    onChatPetugasClick: () -> Unit,
    onNotifikasiClick: () -> Unit,
    onBeriPenilaianClick: () -> Unit,
    onDismissReviewClick: () -> Unit,
    onHospitalCardClick: (HospitalItem) -> Unit,
    onQuickActionTab: (HomeTab) -> Unit,
    onSetQueuePhase: (QueuePhase) -> Unit,
    onCompleteQueueForReview: () -> Unit,
    onOpenCancelSheet: () -> Unit
) {
    // Single Scrollable Column so RTCurvedHeader children (Name Card, Bell, Search Bar) are NEVER blocked by an overlay!
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        RTCurvedHeader(
            height = 236.dp,
            bottomCornerRadius = 34.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Clickable Name Card ("NP | Selamat pagi 👋 | Nama Pasien") -> Opens Profile Tab!
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .springClickable(onClick = onProfileHeaderClick)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(RTColors.AvatarTeal)
                                .border(1.5.dp, RTColors.PureWhite.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.patientProfile.initials,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.PureWhite
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = uiState.greetingTitle,
                                fontSize = 12.5.sp,
                                color = RTColors.PureWhite.copy(alpha = 0.92f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.patientProfile.fullName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.PureWhite
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Lihat Profil",
                                    tint = RTColors.PureWhite.copy(alpha = 0.85f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Clickable Notification Bell with Red Dot Indicator -> Opens NotifikasiScreen!
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .springClickable(onClick = onNotifikasiClick)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifikasi",
                            tint = RTColors.PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                        if (uiState.unreadNotificationCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-5).dp, y = 5.dp)
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(RTColors.NotificationRedDot)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Pill Search Bar (Typeable & Clickable!)
                RTPillSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChanged,
                    onSearchSubmit = onDaftarAntreanClick
                )
            }
        }

        // Dashboard Content overlapping the bottom of RTCurvedHeader by -42.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-42).dp)
                .padding(horizontal = 20.dp)
        ) {
            val currentQueue = uiState.activeQueue
            val completedReview = uiState.completedConsultation

            if (currentQueue != null) {
                RTQueueCard(
                    queue = currentQueue,
                    onPantauClick = onPantauClick,
                    onShowQrTicketClick = onShowQrTicketClick,
                    onAturPengingatClick = onAturPengingatClick,
                    onChatPetugasClick = onChatPetugasClick
                )

                Spacer(modifier = Modifier.height(10.dp))

                UasStateMachineSimulatorBar(
                    currentPhase = currentQueue.phase,
                    onPhaseSelected = onSetQueuePhase,
                    onFinishQueueClick = onCompleteQueueForReview,
                    onCancelQueueClick = onOpenCancelSheet
                )
            } else if (completedReview != null) {
                RTPostConsultationReviewCard(
                    completed = completedReview,
                    onBeriPenilaianClick = onBeriPenilaianClick,
                    onDismissClick = onDismissReviewClick
                )
            } else {
                RTBerandaEmptyQueueCard(
                    onDaftarAntreanClick = onDaftarAntreanClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section "Layanan" - 100% Consistent Mint Surface (#E0F7F4) & Brand Primary Teal Icon (#0A8F82)
            Text(
                text = "Layanan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RTQuickActionItem(
                    title = "Cari RS",
                    backgroundColor = RTColors.QuickActionUniformBg,
                    icon = Icons.Outlined.AddBox,
                    iconTint = RTColors.BrandPrimaryTeal,
                    onClick = onDaftarAntreanClick
                )
                RTQuickActionItem(
                    title = "Jadwal",
                    backgroundColor = RTColors.QuickActionUniformBg,
                    icon = Icons.Outlined.CalendarToday,
                    iconTint = RTColors.BrandPrimaryTeal,
                    onClick = onDaftarAntreanClick
                )
                RTQuickActionItem(
                    title = "Riwayat",
                    backgroundColor = RTColors.QuickActionUniformBg,
                    icon = Icons.Outlined.Schedule,
                    iconTint = RTColors.BrandPrimaryTeal,
                    onClick = { onQuickActionTab(HomeTab.RIWAYAT) }
                )
                RTQuickActionItem(
                    title = "Profil",
                    backgroundColor = RTColors.QuickActionUniformBg,
                    icon = Icons.Outlined.PersonOutline,
                    iconTint = RTColors.BrandPrimaryTeal,
                    onClick = { onQuickActionTab(HomeTab.PROFIL) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section "Poli populer"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Poli populer",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                Text(
                    text = "Lihat semua",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.BrandPrimaryTeal,
                    modifier = Modifier
                        .springClickable(onClick = onDaftarAntreanClick)
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                uiState.popularPoliList.forEach { poli ->
                    val isSelected = poli == uiState.selectedPoliChip
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .springClickable {
                                onPoliChipSelected(poli)
                            }
                            .clip(RoundedCornerShape(19.dp))
                            .background(
                                if (isSelected) RTColors.BrandPrimaryTeal else RTColors.PureWhite
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                shape = RoundedCornerShape(19.dp)
                            )
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = poli,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) RTColors.PureWhite else RTColors.NavyHeading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section "Rumah sakit terdekat"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rumah sakit terdekat",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                if (uiState.searchQuery.isNotBlank()) {
                    Text(
                        text = "Filter: '${uiState.searchQuery}'",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTColors.BrandPrimaryTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    RTShimmerCardPlaceholder(
                        modifier = Modifier.weight(1f),
                        height = 126.dp,
                        cornerRadius = 18.dp
                    )
                    RTShimmerCardPlaceholder(
                        modifier = Modifier.weight(1f),
                        height = 126.dp,
                        cornerRadius = 18.dp
                    )
                }
            } else if (uiState.nearbyHospitals.isEmpty()) {
                RTFloatingCard(cornerRadius = 20.dp, contentPadding = PaddingValues(12.dp)) {
                    RTEmptyStateView(
                        icon = Icons.Outlined.Search,
                        title = "Rumah Sakit Tidak Ditemukan",
                        description = "Tidak ada RS terdekat yang cocok dengan pencarian '${uiState.searchQuery}'.",
                        actionButtonText = "Reset Pencarian",
                        onActionClick = { onSearchQueryChanged("") }
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    uiState.nearbyHospitals.take(2).forEach { hospital ->
                        RTHospitalNearbyCard(
                            hospital = hospital,
                            onClick = { onHospitalCardClick(hospital) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun UasStateMachineSimulatorBar(
    currentPhase: QueuePhase,
    onPhaseSelected: (QueuePhase) -> Unit,
    onFinishQueueClick: () -> Unit,
    onCancelQueueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RTColors.PureWhite)
            .border(1.dp, RTColors.InputBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Panel Interaktif Simulasi Antrean (Klik untuk uji fase):",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.SlateBodyText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val phases = listOf(
                QueuePhase.MENUNGGU to "1. Menunggu (>3)",
                QueuePhase.PERINGATAN_DEKAT to "2. Dekat (≤3)",
                QueuePhase.DIPANGGIL to "3. Dipanggil (QR)",
                QueuePhase.DIJEDA to "4. Dijeda (Darurat)"
            )
            phases.forEach { (phase, label) ->
                val active = currentPhase == phase
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .springClickable { onPhaseSelected(phase) }
                        .clip(RoundedCornerShape(15.dp))
                        .background(if (active) RTColors.BrandPrimaryTeal else RTColors.InputBackground)
                        .border(
                            1.dp,
                            if (active) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                            RoundedCornerShape(15.dp)
                        )
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) RTColors.PureWhite else RTColors.NavyHeading
                    )
                }
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .springClickable(onClick = onFinishQueueClick)
                    .clip(RoundedCornerShape(15.dp))
                    .background(RTColors.WarningAmberBg)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "5. Selesai & Nilai ★",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.WarningAmberText
                )
            }

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .springClickable(onClick = onCancelQueueClick)
                    .clip(RoundedCornerShape(15.dp))
                    .background(RTColors.ErrorSurface)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Batalkan",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.ErrorRed
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun RTPillSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = RTColors.NavyHeading),
        cursorBrush = SolidColor(RTColors.BrandPrimaryTeal),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchSubmit() }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = RTColors.CardSpotShadow,
                        ambientColor = RTColors.CardAmbientShadow
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(RTColors.PureWhite)
                    .padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Cari",
                    tint = RTColors.BrandPrimaryTeal,
                    modifier = Modifier.size(21.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Cari rumah sakit atau poli...",
                            fontSize = 13.5.sp,
                            color = RTColors.MutedSlate
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .springClickable { onQueryChange("") }
                            .clip(CircleShape)
                            .background(RTColors.InputBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Hapus",
                            tint = RTColors.SlateBodyText,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .springClickable(onClick = onSearchSubmit)
                        .clip(RoundedCornerShape(17.dp))
                        .background(RTColors.QuickActionUniformBg)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cari RS",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.BrandPrimaryTeal
                    )
                }
            }
        }
    )
}

@Suppress("FunctionName")
@Composable
private fun RTQuickActionItem(
    title: String,
    backgroundColor: Color,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.springClickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor)
                .border(1.dp, RTColors.MintSurfaceActive, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading
        )
    }
}

@Suppress("FunctionName")
@Composable
private fun RTHospitalNearbyCard(
    hospital: HospitalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPreview = LocalInspectionMode.current

    Surface(
        modifier = modifier
            .springClickable(onClick = onClick)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = RTColors.CardSpotShadow,
                ambientColor = RTColors.CardAmbientShadow
            ),
        shape = RoundedCornerShape(18.dp),
        color = RTColors.PureWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(RTColors.QuickActionUniformBg),
                contentAlignment = Alignment.Center
            ) {
                if (!isPreview) {
                    val resId = when (hospital.id) {
                        "rs-sehat-sidoarjo" -> R.drawable.img_rs_sehat_sidoarjo
                        "rs-harapan-bunda" -> R.drawable.img_rs_harapan_bunda
                        else -> R.drawable.img_rs_medika
                    }
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = hospital.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(RTColors.BrandPrimaryTeal, RTColors.BrandLightTeal)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = null,
                            tint = RTColors.PureWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hospital.name,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = RTColors.SlateBodyText,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${hospital.rating} · ${hospital.distanceKm}",
                    fontSize = 11.5.sp,
                    color = RTColors.SlateBodyText
                )
            }
        }
    }
}

/**
 * Interactive Pantau Antrean Tab with:
 * - Zero Z-order touch interception (Back Button & QR Button inside single scrollable Column)
 * - Live Auto-Countdown Timer Toggle ("Simulasi Otomatis Real-Time")
 * - Interactive Step-by-Step Queue Progress Timeline
 * - Clean bottom spacing so no buttons are ever clipped
 */
@Suppress("FunctionName")
@Composable
private fun PantauAntreanTab(
    queue: ActiveQueue?,
    onBackClick: () -> Unit,
    onDaftarAntreanBaruClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onSetQueuePhase: (QueuePhase) -> Unit,
    onCompleteQueueClick: () -> Unit,
    onShowQrTicketClick: () -> Unit,
    onDatangKeLoketClick: () -> Unit,
    onChatPetugasClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    onCancelQueueClick: () -> Unit
) {
    var autoSimulateEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(autoSimulateEnabled, queue?.remainingQueues, queue?.phase) {
        if (autoSimulateEnabled && queue != null && queue.phase != QueuePhase.DIJEDA) {
            delay(3500)
            if (queue.remainingQueues > 0) {
                onRefreshClick()
            } else {
                autoSimulateEnabled = false
            }
        }
    }

    if (queue == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RTColors.ScreenBackground)
        ) {
            RTCurvedHeader(
                height = 148.dp,
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
                            text = "Pantau Antrean",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Status nomor antrean Anda",
                            fontSize = 12.5.sp,
                            color = RTColors.PureWhite.copy(alpha = 0.92f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(172.dp)
                        .clip(CircleShape)
                        .background(RTColors.QuickActionUniformBg),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(RTColors.MintSurfaceActive),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Assignment,
                            contentDescription = "Belum ada antrean aktif",
                            tint = RTColors.BrandPrimaryTeal,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Belum ada antrean aktif",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Daftar ke poli pilihan Anda untuk\nmendapatkan nomor antrean.",
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp,
                    color = RTColors.SlateBodyText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                RTPrimaryButton(
                    text = "Daftar antrean baru",
                    onClick = onDaftarAntreanBaruClick
                )
            }
        }
        return
    }

    val isPaused = queue.phase == QueuePhase.DIJEDA
    val ringColor = if (isPaused) RTColors.WarningAmberBorder else RTColors.BrandPrimaryTeal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header inside single scrollable Column so Back & QR buttons are 100% clickable!
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
                        text = "Pantau Antrean",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${queue.poliName} · ${queue.hospitalName}",
                        fontSize = 12.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.92f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .springClickable(onClick = onShowQrTicketClick)
                        .clip(CircleShape)
                        .background(RTColors.PureWhite.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Tiket QR",
                        tint = RTColors.PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-36).dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isPaused) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(RTColors.WarningAmberBg)
                        .border(1.2.dp, RTColors.WarningAmberBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(RTColors.WarningAmberBorder),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = null,
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Antrean dijeda sementara",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.WarningAmberText
                            )
                            Text(
                                text = "Dokter sedang menangani keadaan mendadak (visite/operasi darurat).",
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = RTColors.WarningAmberText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(RTColors.WarningAmberBorder.copy(alpha = 0.22f))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Dijeda sejak 09:40 · Est. ± 15 menit",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.WarningAmberText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            RTFloatingCard(
                cornerRadius = 22.dp,
                contentPadding = PaddingValues(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Row inside Monitor Card: Status + Live Auto-Simulation Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isPaused) "Status antrean" else "Sisa Antrean",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ringColor
                        )

                        Row(
                            modifier = Modifier
                                .height(30.dp)
                                .springClickable { autoSimulateEnabled = !autoSimulateEnabled }
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    if (autoSimulateEnabled) RTColors.BrandPrimaryTeal else RTColors.QuickActionUniformBg
                                )
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (autoSimulateEnabled) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (autoSimulateEnabled) RTColors.PureWhite else RTColors.BrandPrimaryTeal,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (autoSimulateEnabled) "Live Auto: ON" else "Simulasi Live",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (autoSimulateEnabled) RTColors.PureWhite else RTColors.BrandPrimaryTeal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .springClickable(onClick = onRefreshClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 11.dp.toPx()
                            drawCircle(
                                color = if (isPaused) RTColors.WarningAmberBg else RTColors.InputBorder,
                                style = Stroke(width = stroke)
                            )
                            val arcAngles = listOf(250f, 340f, 70f, 160f)
                            arcAngles.forEach { startAngle ->
                                drawArc(
                                    color = ringColor,
                                    startAngle = startAngle,
                                    sweepAngle = 40f,
                                    useCenter = false,
                                    topLeft = Offset(stroke / 2, stroke / 2),
                                    size = Size(size.width - stroke, size.height - stroke),
                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                )
                            }
                        }

                        if (isPaused) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Dijeda",
                                tint = RTColors.WarningAmberBorder,
                                modifier = Modifier.size(44.dp)
                            )
                        } else {
                            AnimatedContent(
                                targetState = queue.remainingQueues,
                                transitionSpec = {
                                    (slideInVertically { h -> h } + fadeIn()).togetherWith(
                                        slideOutVertically { h -> -h } + fadeOut()
                                    ).using(SizeTransform(clip = false))
                                },
                                label = "RemainingCountRing"
                            ) { count ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = count.toString(),
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = RTColors.BrandPrimaryTeal
                                    )
                                    Text(
                                        text = "Ketuk -1",
                                        fontSize = 10.sp,
                                        color = RTColors.MutedSlate
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isPaused) {
                            "Estimasi: belum dapat ditentukan"
                        } else {
                            "Estimasi ${queue.dynamicWaitRangeText}"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPaused) RTColors.WarningAmberText else RTColors.BrandPrimaryTeal
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isPaused) "Dilayani (dijeda)" else "Sedang dilayani",
                                fontSize = 11.5.sp,
                                color = RTColors.SlateBodyText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = queue.currentServedNumber,
                                transitionSpec = {
                                    (slideInVertically { h -> h } + fadeIn()).togetherWith(
                                        slideOutVertically { h -> -h } + fadeOut()
                                    )
                                },
                                label = "MonitorServedFlip"
                            ) { served ->
                                Text(
                                    text = served,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RTColors.NavyHeading
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .springClickable(onClick = onShowQrTicketClick)
                                .clip(RoundedCornerShape(14.dp))
                                .background(RTColors.QuickActionUniformBg)
                                .border(1.dp, RTColors.MintSurfaceActive, RoundedCornerShape(14.dp))
                                .padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nomor Anda (Klik QR)",
                                fontSize = 11.5.sp,
                                color = RTColors.BrandPrimaryTeal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = queue.userQueueNumber,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            UasStateMachineSimulatorBar(
                currentPhase = queue.phase,
                onPhaseSelected = onSetQueuePhase,
                onFinishQueueClick = onCompleteQueueClick,
                onCancelQueueClick = onCancelQueueClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (isPaused) {
                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(18.dp)) {
                    Text(
                        text = "Apa selanjutnya?",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PausedStepRow("1", "Petugas memperbarui status dokter")
                    Spacer(modifier = Modifier.height(8.dp))
                    PausedStepRow("2", "Anda mendapat notifikasi saat dilanjutkan")
                    Spacer(modifier = Modifier.height(8.dp))
                    PausedStepRow("3", "Anda dapat menjadwal ulang atau membatalkan")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onChatPetugasClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.BrandPrimaryTeal),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Hubungi petugas",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }

                    OutlinedButton(
                        onClick = onCancelQueueClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.ErrorRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Batalkan antrean",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.ErrorRed
                        )
                    }
                }
            } else {
                RTFloatingCard(
                    cornerRadius = 22.dp,
                    contentPadding = PaddingValues(18.dp)
                ) {
                    Text(
                        text = "Urutan berikutnya (Ketuk Refresh / Lingkaran untuk maju)",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        queue.upcomingSequence.forEach { code ->
                            val isSelf = code == queue.userQueueNumber
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(17.dp))
                                    .background(
                                        if (isSelf) RTColors.BrandPrimaryTeal else RTColors.PureWhite
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelf) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                        shape = RoundedCornerShape(17.dp)
                                    )
                                    .padding(horizontal = 13.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = code,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelf) RTColors.PureWhite else RTColors.NavyHeading
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(RTColors.QuickActionUniformBg)
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = RTColors.BrandPrimaryTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Notifikasi dikirim otomatis saat sisa antrean ≤ 2",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRefreshClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.BrandPrimaryTeal),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = RTColors.PureWhite,
                            contentColor = RTColors.BrandPrimaryTeal
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .tactilePressScale()
                    ) {
                        Text(
                            text = "Maju 1 Antrean",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        RTPrimaryButton(
                            text = if (queue.phase == QueuePhase.DIPANGGIL) {
                                "Datang ke Loket"
                            } else {
                                "Lihat Tiket QR"
                            },
                            onClick = if (queue.phase == QueuePhase.DIPANGGIL) {
                                onDatangKeLoketClick
                            } else {
                                onShowQrTicketClick
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRescheduleClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RTColors.InputBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Text(
                            text = "Reschedule",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.NavyHeading
                        )
                    }

                    OutlinedButton(
                        onClick = onCancelQueueClick,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RTColors.ErrorRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Text(
                            text = "Batalkan Antrean",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.ErrorRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun PausedStepRow(num: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(RTColors.MintSurfaceActive),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = num,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.BrandPrimaryTeal
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = RTColors.NavyHeading
        )
    }
}

/**
 * Riwayat Antrean Tab with Back Button & Clean Empty State on Fresh Accounts
 */
@Suppress("FunctionName")
@Composable
private fun RiwayatAntreanTab(
    historyList: List<QueueHistoryItem>,
    selectedFilter: Int,
    onBackClick: () -> Unit,
    onFilterSelected: (Int) -> Unit,
    onPantauClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDaftarBaruClick: () -> Unit
) {
    val activeItems = historyList.filter { it.status == QueueStatus.ACTIVE || it.status == QueueStatus.PAUSED }
    val completedItems = historyList.filter { it.status == QueueStatus.COMPLETED }
    val cancelledItems = historyList.filter { it.status == QueueStatus.CANCELLED }

    val filters = listOf(
        "Aktif (${activeItems.size})",
        "Selesai (${completedItems.size})",
        "Batal (${cancelledItems.size})"
    )

    val displayedList = when (selectedFilter) {
        0 -> activeItems
        1 -> completedItems
        else -> cancelledItems
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        RTCurvedHeader(
            height = 165.dp,
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
                        text = "Riwayat Antrean",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Semua pendaftaran Anda",
                        fontSize = 12.5.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-34).dp)
                .padding(horizontal = 20.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = RTColors.PureWhite,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filters.forEachIndexed { index, label ->
                        val active = index == selectedFilter
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .springClickable { onFilterSelected(index) }
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                    if (active) RTColors.BrandPrimaryTeal else Color.Transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (active) RTColors.PureWhite else RTColors.SlateBodyText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (displayedList.isEmpty()) {
                val (emptyTitle, emptyDesc) = when (selectedFilter) {
                    0 -> "Belum Ada Antrean Aktif" to "Anda belum memiliki jadwal antrean aktif hari ini. Silakan daftar ke rumah sakit dan poli pilihan Anda."
                    1 -> "Belum Ada Riwayat Selesai" to "Riwayat pemeriksaan dokter yang telah Anda selesaikan akan tercatat secara otomatis di halaman ini."
                    else -> "Riwayat Batal Kosong" to "Tidak ada riwayat antrean yang dibatalkan. Nomor antrean yang dibatalkan akan muncul di sini."
                }
                RTFloatingCard(cornerRadius = 22.dp, contentPadding = PaddingValues(12.dp)) {
                    RTEmptyStateView(
                        icon = if (selectedFilter == 2) Icons.Outlined.EventBusy else Icons.Outlined.History,
                        title = emptyTitle,
                        description = emptyDesc,
                        actionButtonText = if (selectedFilter == 0) "Daftar Antrean Sekarang" else null,
                        onActionClick = if (selectedFilter == 0) onDaftarBaruClick else null
                    )
                }
            } else {
                displayedList.forEach { item ->
                    val isPaused = item.status == QueueStatus.PAUSED
                    val isCancelled = item.status == QueueStatus.CANCELLED
                    val accentColor = when {
                        isCancelled -> RTColors.ErrorRed
                        isPaused -> RTColors.PausedAmber
                        else -> RTColors.BrandPrimaryTeal
                    }

                    RTFloatingCard(
                        cornerRadius = 20.dp,
                        contentPadding = PaddingValues(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.hospitalName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        when {
                                            isCancelled -> RTColors.ErrorSurface
                                            isPaused -> RTColors.PausedBadgeBg
                                            else -> RTColors.MintSurfaceActive
                                        }
                                    )
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = when (item.status) {
                                        QueueStatus.PAUSED -> "Dijeda"
                                        QueueStatus.COMPLETED -> "Selesai"
                                        QueueStatus.CANCELLED -> "Batal"
                                        QueueStatus.ACTIVE -> "Aktif"
                                    },
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.poliAndDate,
                            fontSize = 12.5.sp,
                            color = RTColors.SlateBodyText
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = RTColors.InputBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.queueNumber,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = accentColor
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = item.remainingInfo,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )
                                Text(
                                    text = item.subStatusInfo,
                                    fontSize = 11.sp,
                                    color = RTColors.MutedSlate
                                )
                            }
                        }

                        if (item.status == QueueStatus.ACTIVE || item.status == QueueStatus.PAUSED) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(RTColors.InputBorder)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(item.progress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(50))
                                        .background(accentColor)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .springClickable(onClick = onPantauClick)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(RTColors.BrandPrimaryTeal),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isPaused) "Lihat info" else "Pantau",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RTColors.PureWhite
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .springClickable(onClick = onCancelClick)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(1.dp, RTColors.ErrorRed, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Batalkan",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RTColors.ErrorRed
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RTColors.QuickActionUniformBg)
                    .border(1.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = RTColors.BrandPrimaryTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Antrean aktif tetap tersimpan secara offline saat Anda keluar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.BrandPrimaryTeal
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun ProfilSayaTab(
    profile: PatientProfile,
    onBackClick: () -> Unit,
    onUbahProfilClick: () -> Unit,
    onNotifikasiClick: () -> Unit,
    onPengingatClick: () -> Unit,
    onChatPetugasClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .verticalScroll(rememberScrollState())
    ) {
        RTCurvedHeader(
            height = 256.dp,
            bottomCornerRadius = 32.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RTHeaderBackButton(onClick = onBackClick)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Profil Saya",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(RTColors.MintSurfaceActive)
                        .border(3.5.dp, RTColors.PureWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.initials,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RTColors.BrandPrimaryTeal
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = profile.fullName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(RTColors.MintSurfaceActive)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = RTColors.BrandPrimaryTeal,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Terverifikasi",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.BrandPrimaryTeal
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            RTFloatingCard(
                cornerRadius = 22.dp,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
            ) {
                ProfileDetailRow(Icons.Outlined.Badge, "NIK", profile.nik)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileDetailRow(Icons.Outlined.CalendarToday, "Tanggal lahir", profile.birthPlaceDate)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileDetailRow(Icons.Outlined.PersonOutline, "Jenis kelamin", profile.gender)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileDetailRow(Icons.Outlined.PhoneAndroid, "No. HP", profile.phone)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileDetailRow(Icons.Outlined.Email, "Email", profile.email)
            }

            Spacer(modifier = Modifier.height(16.dp))

            RTFloatingCard(
                cornerRadius = 22.dp,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                ProfileMenuActionRow(Icons.Outlined.PersonOutline, "Ubah profil", null, onUbahProfilClick)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileMenuActionRow(Icons.Outlined.Notifications, "Notifikasi", "Aktif", onNotifikasiClick)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileMenuActionRow(Icons.Outlined.Schedule, "Pengingat pemeriksaan", "3 aktif", onPengingatClick)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileMenuActionRow(Icons.Outlined.ChatBubbleOutline, "Bantuan & chat petugas", null, onChatPetugasClick)
                HorizontalDivider(color = RTColors.InputBorder)
                ProfileMenuActionRow(Icons.Outlined.Info, "Tentang aplikasi", "v1.0.0", {})
            }

            Spacer(modifier = Modifier.height(18.dp))

            RTPrimaryButton(
                text = "Ubah Profil",
                onClick = onUbahProfilClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLogoutClick,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.ErrorRed),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = RTColors.PureWhite,
                    contentColor = RTColors.ErrorRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .tactilePressScale()
            ) {
                Text(
                    text = "Keluar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.ErrorRed
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun ProfileDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(RTColors.QuickActionUniformBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = RTColors.BrandPrimaryTeal,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.5.sp,
                color = RTColors.MutedSlate
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading
            )
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun ProfileMenuActionRow(
    icon: ImageVector,
    title: String,
    badgeText: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .springClickable(onClick = onClick)
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(RTColors.QuickActionUniformBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = RTColors.BrandPrimaryTeal,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading,
            modifier = Modifier.weight(1f)
        )
        if (badgeText != null) {
            Text(
                text = badgeText,
                fontSize = 12.sp,
                color = RTColors.SlateBodyText
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = RTColors.MutedSlate
        )
    }
}
