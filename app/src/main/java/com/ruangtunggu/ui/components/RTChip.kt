package com.ruangtunggu.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.ChipShape
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Filter chip with pill shape. Selected → filled primary; unselected → outlined.
 *
 * @param text        Label.
 * @param selected    Whether the chip is selected.
 * @param onClick     Click callback.
 * @param leadingIcon Optional leading icon vector.
 * @param modifier    Outer modifier.
 */
@Composable
fun RTChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    val containerColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val contentColor = if (selected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val border = if (selected) null
    else BorderStroke(Dimens.BorderWidth, MaterialTheme.colorScheme.outline)

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ChipShape,
        color = containerColor,
        contentColor = contentColor,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )
                Spacer(Modifier.width(Dimens.SpacingXs))
            }
            if (selected && leadingIcon == null) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )
                Spacer(Modifier.width(Dimens.SpacingXs))
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Chip Selected – Light", showBackground = true)
@Composable
private fun PreviewChipSelectedLight() {
    RuangTungguTheme(darkTheme = false) {
        RTChip(
            text = "Poli Umum",
            selected = true,
            onClick = {},
            modifier = Modifier.padding(Dimens.SpacingSm)
        )
    }
}

@Preview(name = "Chip Unselected – Light", showBackground = true)
@Composable
private fun PreviewChipUnselectedLight() {
    RuangTungguTheme(darkTheme = false) {
        RTChip(
            text = "Poli Gigi",
            selected = false,
            onClick = {},
            modifier = Modifier.padding(Dimens.SpacingSm)
        )
    }
}

@Preview(name = "Chip with Icon – Dark", showBackground = true)
@Composable
private fun PreviewChipWithIconDark() {
    RuangTungguTheme(darkTheme = true) {
        RTChip(
            text = "RS Bunda",
            selected = false,
            onClick = {},
            leadingIcon = Icons.Filled.LocalHospital,
            modifier = Modifier.padding(Dimens.SpacingSm)
        )
    }
}
