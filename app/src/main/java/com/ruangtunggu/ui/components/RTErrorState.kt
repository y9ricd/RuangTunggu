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
import androidx.compose.material.icons.filled.ErrorOutline
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
 * Centered error state display with error icon, title, message, and retry button.
 *
 * @param title Headline error text.
 * @param subtitle Detail or explanation of the failure.
 * @param onRetry Callback triggered when user clicks the retry button.
 * @param modifier Outer modifier.
 * @param icon Image vector for the error.
 * @param retryText Label for the retry button.
 */
@Composable
fun RTErrorState(
    title: String,
    subtitle: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.ErrorOutline,
    retryText: String = "Coba Lagi"
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
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.error
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

        Spacer(modifier = Modifier.height(Dimens.SpacingXl))

        RTButton(
            text = retryText,
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(0.5f)
        )
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Error State - Light", showBackground = true)
@Composable
private fun RTErrorStateLightPreview() {
    RuangTungguTheme(darkTheme = false) {
        RTErrorState(
            title = "Koneksi Terputus",
            subtitle = "Tidak dapat terhubung ke server. Pastikan koneksi internet Anda aktif.",
            onRetry = {}
        )
    }
}

@Preview(name = "Error State - Dark", showBackground = true)
@Composable
private fun RTErrorStateDarkPreview() {
    RuangTungguTheme(darkTheme = true) {
        RTErrorState(
            title = "Gagal Memuat Antrean",
            subtitle = "Terjadi kesalahan internal pada server. Silakan coba beberapa saat lagi.",
            onRetry = {}
        )
    }
}

