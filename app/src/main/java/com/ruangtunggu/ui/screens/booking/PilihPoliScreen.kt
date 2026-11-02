package com.ruangtunggu.ui.screens.booking

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.R
import com.ruangtunggu.domain.model.PoliItem
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun PilihPoliScreen(
    hospitalId: String,
    hospitalName: String,
    onBackClick: () -> Unit,
    onContinueToDokter: (PoliItem) -> Unit,
    viewModel: PilihPoliViewModel = hiltViewModel()
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    val selected = uiState.selectedPoli
                    RTPrimaryButton(
                        text = if (selected != null) {
                            "Pilih ${selected.name} →"
                        } else {
                            "Pilih poli terlebih dahulu"
                        },
                        enabled = selected != null,
                        onClick = { selected?.let(onContinueToDokter) }
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
                            text = "Pilih Poli",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Langkah 2 dari 3",
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
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(RTColors.QuickActionCariRsBg)
                        ) {
                            if (!isPreview) {
                                Image(
                                    painter = painterResource(
                                        id = if (hospitalId.contains("harapan")) {
                                            R.drawable.img_rs_harapan_bunda
                                        } else if (hospitalId.contains("medika")) {
                                            R.drawable.img_rs_medika
                                        } else {
                                            R.drawable.img_rs_sehat_sidoarjo
                                        }
                                    ),
                                    contentDescription = hospitalName,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = hospitalName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.NavyHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = RTColors.SlateBodyText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "4.8 · 1,2 km · Buka 24 jam",
                                    fontSize = 12.sp,
                                    color = RTColors.SlateBodyText
                                )
                            }
                        }

                        Text(
                            text = "Ganti",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.BrandPrimaryTeal,
                            modifier = Modifier
                                .springClickable(onClick = onBackClick)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Poli tersedia",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    Text(
                        text = "${uiState.poliList.size} poli",
                        fontSize = 12.5.sp,
                        color = RTColors.SlateBodyText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                uiState.poliList.forEach { poli ->
                    val isSelected = poli.id == uiState.selectedPoli?.id
                    val icon = when (poli.shortName) {
                        "Umum" -> Icons.Outlined.PersonOutline
                        "Anak", "Kandungan" -> Icons.Outlined.FavoriteBorder
                        "Gigi" -> Icons.Outlined.AddBox
                        "Mata" -> Icons.Outlined.Search
                        else -> Icons.Outlined.Schedule
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .springClickable { viewModel.onPoliSelected(poli) }
                            .border(
                                width = if (isSelected) 1.8.dp else 1.dp,
                                color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.PureWhite,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        color = RTColors.PureWhite,
                        shadowElevation = if (isSelected) 8.dp else 3.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(RTColors.QuickActionUniformBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = poli.name,
                                    tint = RTColors.BrandPrimaryTeal,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = poli.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RTColors.NavyHeading
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${poli.doctorCount} dokter · mulai ${poli.startHour}",
                                    fontSize = 12.5.sp,
                                    color = RTColors.SlateBodyText
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (isSelected) 6.dp else 1.8.dp,
                                        color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
