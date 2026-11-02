package com.ruangtunggu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Pill-shaped search bar for hospitals and polyclinics.
 *
 * @param query Current text input.
 * @param onQueryChange Callback invoked when text changes.
 * @param modifier Outer modifier.
 * @param placeholder Hint text when empty.
 * @param onSearch Optional callback when IME Search is pressed.
 */
@Composable
fun RTSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Cari rumah sakit atau poli...",
    onSearch: ((String) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = Dimens.SpacingLg),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Cari",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimens.IconSizeMedium)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.SpacingMd),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            onSearch?.invoke(query)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(Dimens.IconSizeMedium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Hapus teks",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Search Bar - Empty (Light)", showBackground = true)
@Composable
private fun RTSearchBarEmptyPreview() {
    RuangTungguTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            RTSearchBar(
                query = "",
                onQueryChange = {}
            )
        }
    }
}

@Preview(name = "Search Bar - Filled (Dark)", showBackground = true)
@Composable
private fun RTSearchBarFilledPreview() {
    RuangTungguTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            RTSearchBar(
                query = "Poli Anak",
                onQueryChange = {}
            )
        }
    }
}

