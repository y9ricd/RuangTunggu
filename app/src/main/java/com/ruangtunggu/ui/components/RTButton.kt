package com.ruangtunggu.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.ButtonShape
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Button variant options for [RTButton].
 */
enum class RTButtonVariant {
    Primary,
    Secondary,
    Text
}

/**
 * Design‑system button with loading state, icon slot and full‑width option.
 *
 * @param text      Button label.
 * @param onClick   Click callback.
 * @param variant   Visual variant – filled, outlined, or text.
 * @param isLoading When `true` the label is replaced by a spinner and clicks are ignored.
 * @param enabled   Whether the button is interactive.
 * @param fullWidth Fill max width when `true`.
 * @param icon      Optional leading icon composable.
 * @param modifier  Outer modifier.
 */
@Composable
fun RTButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: RTButtonVariant = RTButtonVariant.Primary,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    icon: (@Composable () -> Unit)? = null
) {
    val baseModifier = modifier
        .heightIn(min = Dimens.TouchTargetMin)
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)

    val contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)

    val content: @Composable () -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = when (variant) {
                    RTButtonVariant.Primary -> MaterialTheme.colorScheme.onPrimary
                    RTButtonVariant.Secondary -> MaterialTheme.colorScheme.primary
                    RTButtonVariant.Text -> MaterialTheme.colorScheme.primary
                }
            )
        } else {
            if (icon != null) {
                icon()
                Spacer(Modifier.width(Dimens.SpacingSm))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    when (variant) {
        RTButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                modifier = baseModifier,
                enabled = enabled && !isLoading,
                shape = ButtonShape,
                contentPadding = contentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = { content() }
            )
        }

        RTButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = baseModifier,
                enabled = enabled && !isLoading,
                shape = ButtonShape,
                contentPadding = contentPadding,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                content = { content() }
            )
        }

        RTButtonVariant.Text -> {
            TextButton(
                onClick = onClick,
                modifier = baseModifier,
                enabled = enabled && !isLoading,
                shape = ButtonShape,
                contentPadding = contentPadding,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                content = { content() }
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Primary – Light", showBackground = true)
@Composable
private fun PreviewPrimaryLight() {
    RuangTungguTheme(darkTheme = false) {
        RTButton(text = "Daftar Antrean", onClick = {})
    }
}

@Preview(name = "Primary – Dark", showBackground = true)
@Composable
private fun PreviewPrimaryDark() {
    RuangTungguTheme(darkTheme = true) {
        RTButton(text = "Daftar Antrean", onClick = {})
    }
}

@Preview(name = "Secondary", showBackground = true)
@Composable
private fun PreviewSecondary() {
    RuangTungguTheme {
        RTButton(text = "Batal", onClick = {}, variant = RTButtonVariant.Secondary)
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun PreviewLoading() {
    RuangTungguTheme {
        RTButton(text = "Memuat…", onClick = {}, isLoading = true)
    }
}

@Preview(name = "Disabled", showBackground = true)
@Composable
private fun PreviewDisabled() {
    RuangTungguTheme {
        RTButton(text = "Tidak Aktif", onClick = {}, enabled = false)
    }
}

@Preview(name = "Text Variant", showBackground = true)
@Composable
private fun PreviewText() {
    RuangTungguTheme {
        RTButton(text = "Lewati", onClick = {}, variant = RTButtonVariant.Text)
    }
}
