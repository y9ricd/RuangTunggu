package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.ui.theme.RTColors

/**
 * Design System Inheritance Rule #1 & RB01_Batalkan_Antrean 1.png:
 * ModalBottomSheet with 24.dp top corner radius on pure white background.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun BatalkanAntreanBottomSheet(
    queue: ActiveQueue,
    onDismiss: () -> Unit,
    onConfirmCancel: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reasons = listOf(
        "Jadwal bentrok",
        "Sudah merasa lebih baik",
        "Salah memilih poli atau dokter",
        "Alasan lainnya"
    )
    var selectedReason by remember { mutableStateOf(reasons.first()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = RTColors.PureWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Batalkan antrean?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Pilih alasan agar kami dapat meningkatkan layanan.",
                fontSize = 13.sp,
                color = RTColors.SlateBodyText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Queue summary box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RTColors.InputBackground)
                    .border(1.dp, RTColors.InputBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(RTColors.MintSurfaceActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Assignment,
                        contentDescription = null,
                        tint = RTColors.BrandPrimaryTeal,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${queue.userQueueNumber} · ${queue.poliName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.NavyHeading
                    )
                    Text(
                        text = "${queue.scheduleDateTime} · ${queue.hospitalName}",
                        fontSize = 12.sp,
                        color = RTColors.SlateBodyText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Alasan pembatalan",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading
            )

            Spacer(modifier = Modifier.height(10.dp))

            reasons.forEach { reason ->
                val isSelected = reason == selectedReason
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .springClickable { selectedReason = reason }
                        .clip(RoundedCornerShape(13.dp))
                        .background(
                            if (isSelected) RTColors.QuickActionRiwayatBg else RTColors.PureWhite
                        )
                        .border(
                            width = if (isSelected) 1.4.dp else 1.dp,
                            color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                            shape = RoundedCornerShape(13.dp)
                        )
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 6.dp else 1.6.dp,
                                color = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.MutedSlate,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = reason,
                        fontSize = 13.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = RTColors.NavyHeading
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Warning Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RTColors.WarningAmberBg)
                    .border(1.dp, RTColors.WarningAmberBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(RTColors.WarningAmberText),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = RTColors.PureWhite,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nomor dihanguskan, kuota slot dikembalikan",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.WarningAmberText
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, RTColors.InputBorder),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .tactilePressScale()
                ) {
                    Text(
                        text = "Tidak jadi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.SlateBodyText
                    )
                }

                Button(
                    onClick = { onConfirmCancel(selectedReason) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RTColors.ErrorRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .tactilePressScale()
                ) {
                    Text(
                        text = "Ya, batalkan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
