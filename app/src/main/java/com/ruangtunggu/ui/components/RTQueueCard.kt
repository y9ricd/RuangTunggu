package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Active queue summary card as seen on the Beranda (Home) dashboard.
 *
 * @param queueNumber Patient's ticket number (e.g. "A-023").
 * @param currentServing Ticket number currently being served (e.g. "A-018").
 * @param poliName Department / clinic name (e.g. "Poli Anak").
 * @param hospitalName Hospital name (e.g. "RS Sehat Sidoarjo").
 * @param date Appointment date / time (e.g. "Kam, 22 Okt · 09:00").
 * @param remainingCount Number of patients ahead (e.g. 5).
 * @param estimatedMinutes Estimated waiting time in minutes (e.g. 25).
 * @param onMonitorClick Action callback to navigate to live queue monitoring.
 * @param modifier Outer modifier.
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
    RTCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLg)
        ) {
            // Header: Badge + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RTBadge(
                    text = "Antrean aktif",
                    status = RTBadgeStatus.ACTIVE
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMd))

            // Main Queue Details: Large Queue Number & Poli
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Nomor Anda",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = queueNumber,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.SpacingSm))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .padding(horizontal = Dimens.SpacingMd, vertical = Dimens.SpacingSm)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Sedang dilayani",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = currentServing,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingSm))

            // Poli & Hospital
            Text(
                text = "$poliName · $hospitalName",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMd))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingMd))

            // Bottom info: Remaining & Estimate + Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sisa $remainingCount antrean · estimasi $estimatedMinutes mnt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                RTButton(
                    text = "Pantau",
                    onClick = onMonitorClick,
                    variant = RTButtonVariant.Primary
                )
            }
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Queue Card - Light", showBackground = true)
@Composable
private fun RTQueueCardLightPreview() {
    RuangTungguTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            RTQueueCard(
                queueNumber = "A-023",
                currentServing = "A-018",
                poliName = "Poli Anak",
                hospitalName = "RS Sehat Sidoarjo",
                date = "Kam, 22 Okt · 09:00",
                remainingCount = 5,
                estimatedMinutes = 25,
                onMonitorClick = {}
            )
        }
    }
}

@Preview(name = "Queue Card - Dark", showBackground = true)
@Composable
private fun RTQueueCardDarkPreview() {
    RuangTungguTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            RTQueueCard(
                queueNumber = "A-023",
                currentServing = "A-018",
                poliName = "Poli Anak",
                hospitalName = "RS Sehat Sidoarjo",
                date = "Kam, 22 Okt · 09:00",
                remainingCount = 5,
                estimatedMinutes = 25,
                onMonitorClick = {}
            )
        }
    }
}

