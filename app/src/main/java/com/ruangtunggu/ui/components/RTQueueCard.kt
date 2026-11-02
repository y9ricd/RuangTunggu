package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandGreenBadge
import com.ruangtunggu.ui.theme.RTBrandGreenText
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal

/**
 * Active queue summary card as seen on the Beranda (Home) dashboard in Figma.
 */
@Composable
fun RTQueueCard(
    queueNumber: String,
    currentServing: String,
    poliName: String,
    hospitalName: String,
    date: String,
    remainingCount: Int,
    estimatedMinutes: Int,
    onMonitorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = Color(0x1F000000),
                ambientColor = Color(0x12000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Badge + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Green pill: ● Antrean aktif
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(RTBrandGreenBadge)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(RTBrandGreenText)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Antrean aktif",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandGreenText
                    )
                }

                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = RTBrandMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Queue Details: Large Queue Number & Currently Serving
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = queueNumber,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RTBrandTeal,
                    letterSpacing = (-0.5).sp
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Sedang dilayani",
                        fontSize = 11.sp,
                        color = RTBrandMuted
                    )
                    Text(
                        text = currentServing,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Department & Hospital
            Text(
                text = "$poliName · $hospitalName",
                fontSize = 13.sp,
                color = RTBrandSlate
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(RTBrandBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.68f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(RTBrandTeal)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Remaining info + Pantau button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sisa $remainingCount antrean · estimasi $estimatedMinutes menit",
                    fontSize = 12.sp,
                    color = RTBrandSlate
                )

                Button(
                    onClick = onMonitorClick,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RTBrandTeal,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "Pantau",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
