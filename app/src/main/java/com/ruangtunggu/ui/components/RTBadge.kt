package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme
import com.ruangtunggu.ui.theme.RuangTungguTheme.extraColors

/**
 * Status of a queue or history item.
 */
enum class RTBadgeStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED,
    PENDING
}

/**
 * A small status badge: coloured dot + label text.
 *
 * @param text   Badge label (e.g. "Aktif", "Selesai").
 * @param status Semantic status that controls colour.
 * @param modifier Outer modifier.
 */
@Composable
fun RTBadge(
    text: String,
    status: RTBadgeStatus,
    modifier: Modifier = Modifier
) {
    val (dotColor, bgColor, textColor) = badgeColors(status)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .semantics { contentDescription = "$text: status $status" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingXs)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}

/**
 * Resolves badge colour triple: dot, background, text.
 */
@Composable
private fun badgeColors(status: RTBadgeStatus): Triple<Color, Color, Color> {
    return when (status) {
        RTBadgeStatus.ACTIVE -> Triple(
            extraColors.queueActive,
            extraColors.queueBadgeBg,
            extraColors.success
        )
        RTBadgeStatus.COMPLETED -> Triple(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )
        RTBadgeStatus.CANCELLED -> Triple(
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error
        )
        RTBadgeStatus.PENDING -> Triple(
            extraColors.warning,
            Color(0xFFFFF3E0), // light amber tint
            extraColors.warning
        )
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Active – Light", showBackground = true)
@Composable
private fun PreviewActiveLight() {
    RuangTungguTheme(darkTheme = false) {
        RTBadge(text = "Aktif", status = RTBadgeStatus.ACTIVE, modifier = Modifier.padding(8.dp))
    }
}

@Preview(name = "Completed – Light", showBackground = true)
@Composable
private fun PreviewCompletedLight() {
    RuangTungguTheme(darkTheme = false) {
        RTBadge(text = "Selesai", status = RTBadgeStatus.COMPLETED, modifier = Modifier.padding(8.dp))
    }
}

@Preview(name = "Cancelled – Dark", showBackground = true)
@Composable
private fun PreviewCancelledDark() {
    RuangTungguTheme(darkTheme = true) {
        RTBadge(text = "Dibatalkan", status = RTBadgeStatus.CANCELLED, modifier = Modifier.padding(8.dp))
    }
}

@Preview(name = "Pending – Dark", showBackground = true)
@Composable
private fun PreviewPendingDark() {
    RuangTungguTheme(darkTheme = true) {
        RTBadge(text = "Menunggu", status = RTBadgeStatus.PENDING, modifier = Modifier.padding(8.dp))
    }
}
