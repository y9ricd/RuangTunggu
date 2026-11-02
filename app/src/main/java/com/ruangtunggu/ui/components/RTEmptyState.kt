package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Centered empty state display with icon, title, subtitle, and optional call to action.
 *
 * @param title Primary heading text.
 * @param subtitle Explanatory message.
 * @param modifier Outer modifier.
 * @param icon Image vector to illustrate empty state.
 * @param actionText Optional button text for primary action.
 * @param onAction Optional callback when action button is pressed.
 */
@Composable
fun RTEmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Inbox,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingScreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingLg))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingSm))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (!actionText.isNullOrBlank() && onAction != null) {
            Spacer(modifier = Modifier.height(Dimens.SpacingXl))
            RTButton(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Empty State - Light", showBackground = true)
@Composable
private fun RTEmptyStateLightPreview() {
    RuangTungguTheme(darkTheme = false) {
        RTEmptyState(
            title = "Belum Ada Antrean Aktif",
            subtitle = "Anda belum mendaftar antrean untuk hari ini. Silakan cari poli untuk mendaftar.",
            actionText = "Cari Poli",
            onAction = {}
        )
    }
}

@Preview(name = "Empty State - Dark", showBackground = true)
@Composable
private fun RTEmptyStateDarkPreview() {
    RuangTungguTheme(darkTheme = true) {
        RTEmptyState(
            title = "Belum Ada Notifikasi",
            subtitle = "Pemberitahuan terkait antrean dan jadwal dokter akan muncul di sini."
        )
    }
}

