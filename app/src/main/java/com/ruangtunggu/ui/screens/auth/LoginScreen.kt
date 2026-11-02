package com.ruangtunggu.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTHeaderWave
import com.ruangtunggu.ui.components.RTLogoCard
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandInputBg
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandTeal

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Routes.Home) {
                popUpTo(Routes.Login) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Top Teal Wave Header with Centered Logo Card ──────────
            RTHeaderWave(
                modifier = Modifier.fillMaxWidth(),
                height = 270.dp,
                waveDip = 0.88f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RTLogoCard(
                        size = 68.dp,
                        cornerRadius = 20.dp,
                        elevation = 8.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Selamat datang kembali",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Masuk untuk mulai antre tanpa menunggu",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.90f)
                    )
                }
            }

            // ── Floating White Form Card ───────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-36).dp)
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = Color(0x1F000000),
                        ambientColor = Color(0x10000000)
                    )
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Field 1: NIK / Email
                    Text(
                        text = "NIK / Email",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FigmaInputField(
                        value = uiState.identifier,
                        onValueChange = viewModel::onIdentifierChanged,
                        placeholder = "Masukkan NIK atau email",
                        leadingIcon = Icons.Outlined.Badge,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Field 2: Kata Sandi
                    Text(
                        text = "Kata sandi",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTBrandNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FigmaInputField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChanged,
                        placeholder = "Masukkan kata sandi",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = RTBrandMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CAPTCHA Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(RTBrandInputBg)
                            .border(1.dp, RTBrandBorder, RoundedCornerShape(12.dp))
                            .clickable { viewModel.onCaptchaToggled(!uiState.isCaptchaVerified) }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = uiState.isCaptchaVerified,
                                onCheckedChange = { viewModel.onCaptchaToggled(it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = RTBrandTeal,
                                    uncheckedColor = RTBrandBorder
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Saya bukan robot",
                                fontSize = 13.sp,
                                color = RTBrandNavy
                            )
                        }

                        Text(
                            text = "CAPTCHA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTBrandMuted,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Button
                    Button(
                        onClick = viewModel::login,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RTBrandTeal,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lupa kata sandi?
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lupa kata sandi?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RTBrandTeal,
                            modifier = Modifier
                                .clickable { navController.navigate(Routes.ForgotPassword) }
                                .padding(6.dp)
                        )
                    }
                }
            }

            // ── Bottom Action Links ────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-18).dp)
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Belum punya akun? Daftar sekarang
                Text(
                    text = "Belum punya akun? Daftar sekarang",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTBrandTeal,
                    modifier = Modifier
                        .clickable { navController.navigate(Routes.Register) }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Footer terms
                Text(
                    text = "Dengan masuk, Anda menyetujui Syarat & Ketentuan",
                    fontSize = 11.sp,
                    color = RTBrandMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Clean Modern Input Field matching Figma styling:
 * Background #F8FAFC, 1dp border #E2E8F0, rounded 12dp, height 50dp.
 */
@Composable
private fun FigmaInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(RTBrandInputBg)
            .border(1.dp, RTBrandBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = RTBrandMuted,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = RTBrandMuted,
                    fontSize = 13.5.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = RTBrandNavy,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(RTBrandTeal),
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
            trailingIcon()
        }
    }
}
