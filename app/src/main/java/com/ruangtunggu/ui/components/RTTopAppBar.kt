package com.ruangtunggu.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Custom TopAppBar wrapper for RuangTunggu.
 *
 * @param title Screen title.
 * @param subtitle Optional subtitle (e.g. "Diperbarui 1 menit lalu").
 * @param showBackButton Whether to show the navigation back button.
 * @param onBackClick Callback invoked when back button is pressed.
 * @param modifier Outer modifier.
 * @param actions Optional action icons in the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RTTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "RTTopAppBar - Simple (Light)", showBackground = true)
@Composable
private fun RTTopAppBarSimplePreview() {
    RuangTungguTheme(darkTheme = false) {
        RTTopAppBar(
            title = "Cari RS & Poli",
            showBackButton = true
        )
    }
}

@Preview(name = "RTTopAppBar - With Subtitle (Dark)", showBackground = true)
@Composable
private fun RTTopAppBarSubtitlePreview() {
    RuangTungguTheme(darkTheme = true) {
        RTTopAppBar(
            title = "Pantau Antrean",
            subtitle = "Diperbarui 1 menit lalu",
            showBackButton = true
        )
    }
}

