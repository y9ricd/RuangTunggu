package com.ruangtunggu.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme
import com.ruangtunggu.ui.theme.TextFieldShape

/**
 * Themed OutlinedTextField with built‑in error display and
 * optional password‑visibility toggle.
 */
@Composable
fun RTTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val effectiveVisualTransformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else -> visualTransformation
    }

    val effectiveTrailingIcon: @Composable (() -> Unit)? = when {
        isPassword -> {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Sembunyikan kata sandi"
                        else
                            "Tampilkan kata sandi"
                    )
                }
            }
        }
        else -> trailingIcon
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = effectiveTrailingIcon,
            isError = isError,
            visualTransformation = effectiveVisualTransformation,
            keyboardOptions = if (isPassword)
                keyboardOptions.copy(keyboardType = KeyboardType.Password)
            else
                keyboardOptions,
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            shape = TextFieldShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error
            )
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = Dimens.SpacingLg, top = Dimens.SpacingXs)
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Default – Light", showBackground = true)
@Composable
private fun PreviewDefaultLight() {
    RuangTungguTheme(darkTheme = false) {
        RTTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "Masukkan email",
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email")
            },
            modifier = Modifier.padding(Dimens.PaddingScreen)
        )
    }
}

@Preview(name = "Error – Light", showBackground = true)
@Composable
private fun PreviewErrorLight() {
    RuangTungguTheme(darkTheme = false) {
        RTTextField(
            value = "invalid",
            onValueChange = {},
            label = "Email",
            isError = true,
            errorMessage = "Format email tidak valid",
            modifier = Modifier.padding(Dimens.PaddingScreen)
        )
    }
}

@Preview(name = "Password – Dark", showBackground = true)
@Composable
private fun PreviewPasswordDark() {
    RuangTungguTheme(darkTheme = true) {
        RTTextField(
            value = "secret123",
            onValueChange = {},
            label = "Kata Sandi",
            isPassword = true,
            modifier = Modifier.padding(Dimens.PaddingScreen)
        )
    }
}
