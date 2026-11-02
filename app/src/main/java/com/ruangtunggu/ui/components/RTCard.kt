package com.ruangtunggu.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.CardShape
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Elevated card styled with the RuangTunggu design system.
 *
 * @param modifier Outer modifier.
 * @param onClick  Optional click handler – makes the card clickable.
 * @param content  Inner composable content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RTCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
    val elevation = CardDefaults.elevatedCardElevation(
        defaultElevation = Dimens.CardElevation
    )

    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            shape = CardShape,
            colors = cardColors,
            elevation = elevation,
            content = { content() }
        )
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = CardShape,
            colors = cardColors,
            elevation = elevation,
            content = { content() }
        )
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Card – Light", showBackground = true)
@Composable
private fun PreviewCardLight() {
    RuangTungguTheme(darkTheme = false) {
        RTCard(
            modifier = Modifier
                .padding(Dimens.PaddingScreen)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(Dimens.SpacingLg)) {
                Text("Judul Kartu", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Deskripsi konten kartu",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.SpacingSm)
                )
            }
        }
    }
}

@Preview(name = "Card – Dark", showBackground = true)
@Composable
private fun PreviewCardDark() {
    RuangTungguTheme(darkTheme = true) {
        RTCard(
            modifier = Modifier
                .padding(Dimens.PaddingScreen)
                .fillMaxWidth(),
            onClick = {}
        ) {
            Column(modifier = Modifier.padding(Dimens.SpacingLg)) {
                Text("Kartu Klik", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Kartu ini bisa diklik",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.SpacingSm)
                )
            }
        }
    }
}
