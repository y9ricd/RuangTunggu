package com.ruangtunggu.ui.screens.booking

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.R
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTEmptyStateView
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTShimmerCardPlaceholder
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun CariRsPoliScreen(
    initialFilter: String = "Semua",
    onBackClick: () -> Unit,
    onContinueToPilihPoli: (HospitalItem) -> Unit,
    viewModel: CariRsPoliViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialFilter) {
        viewModel.initializeFilter(initialFilter)
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
                    RTPrimaryButton(
                        text = "Pilih rumah sakit & poli",
                        enabled = uiState.selectedHospital != null && !uiState.isLoading,
                        onClick = {
                            uiState.selectedHospital?.let(onContinueToPilihPoli)
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
        ) {
            RTCurvedHeader(
                height = 198.dp,
                bottomCornerRadius = 32.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RTHeaderBackButton(onClick = onBackClick)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Cari RS & Poli",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.PureWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Temukan layanan terdekat",
                                fontSize = 12.5.sp,
                                color = RTColors.PureWhite.copy(alpha = 0.92f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Search Bar inside Wave Header
                    BasicTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = RTColors.NavyHeading),
                        cursorBrush = SolidColor(RTColors.BrandPrimaryTeal),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .shadow(8.dp, RoundedCornerShape(16.dp))
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(RTColors.PureWhite)
                                    .padding(horizontal = 16.dp),
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
                                    if (uiState.searchQuery.isEmpty()) {
                                        Text(
                                            text = "Nama RS atau poli",
                                            fontSize = 14.sp,
                                            color = RTColors.MutedSlate
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Horizontal Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    uiState.filterChips.forEach { chip ->
                        val selected = chip == uiState.selectedFilterChip
                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .springClickable { viewModel.onFilterChipSelected(chip) }
                                .clip(RoundedCornerShape(19.dp))
                                .background(if (selected) RTColors.BrandPrimaryTeal else RTColors.PureWhite)
                                .border(
                                    width = 1.dp,
                                    color = if (selected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                    shape = RoundedCornerShape(19.dp)
                                )
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chip,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) RTColors.PureWhite else RTColors.NavyHeading
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (uiState.isLoading) {
                    Text(
                        text = "Memuat daftar rumah sakit...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.SlateBodyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    repeat(3) {
                        RTShimmerCardPlaceholder(height = 116.dp, cornerRadius = 20.dp)
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                } else if (uiState.hospitals.isEmpty()) {
                    // Empty State matching Cari RS gagal.png & Rule #3
                    RTEmptyStateView(
                        icon = Icons.Outlined.SearchOff,
                        title = "Hasil Pencarian Tidak Ditemukan",
                        description = "Kami tidak menemukan rumah sakit atau poli untuk kata kunci '${uiState.searchQuery}'. Coba kata kunci lain atau reset filter.",
                        actionButtonText = "Reset Pencarian",
                        onActionClick = viewModel::onResetSearch
                    )
                } else {
                    Text(
                        text = "${uiState.hospitals.size} hasil ditemukan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.SlateBodyText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    uiState.hospitals.forEach { hospital ->
                        val isSelected = hospital.id == uiState.selectedHospital?.id
                        HospitalSelectionCard(
                            hospital = hospital,
                            isSelected = isSelected,
                            onClick = { viewModel.onHospitalSelected(hospital) }
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun HospitalSelectionCard(
    hospital: HospitalItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isPreview = LocalInspectionMode.current
    val resId = when (hospital.drawableResName) {
        "img_rs_sehat_sidoarjo" -> R.drawable.img_rs_sehat_sidoarjo
        "img_rs_harapan_bunda" -> R.drawable.img_rs_harapan_bunda
        else -> R.drawable.img_rs_medika
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .springClickable(onClick = onClick)
            .shadow(
                elevation = if (isSelected) 12.dp else 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = RTColors.CardSpotShadow,
                ambientColor = RTColors.CardAmbientShadow
            )
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.PureWhite,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = RTColors.PureWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RTColors.QuickActionCariRsBg)
            ) {
                if (!isPreview) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = hospital.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hospital.name,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(RTColors.BrandPrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Terpilih",
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RTColors.SlateBodyText,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${hospital.rating} · ${hospital.distanceKm} · ${hospital.openHours}",
                        fontSize = 12.sp,
                        color = RTColors.SlateBodyText
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    hospital.availablePoli.take(3).forEach { poliTag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(RTColors.QuickActionRiwayatBg)
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = poliTag,
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
}
