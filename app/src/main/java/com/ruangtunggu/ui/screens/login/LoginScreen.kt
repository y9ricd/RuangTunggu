package com.ruangtunggu.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.ui.components.CloudflareTurnstileCaptcha
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTTextField
import com.ruangtunggu.ui.components.RuangTungguLogoCard
import com.ruangtunggu.ui.components.springClickable
import com.ruangtunggu.ui.components.tactilePressScale
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreenContent(
        uiState = uiState,
        onNikOrEmailChanged = viewModel::onNikOrEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onCaptchaChanged = viewModel::onCaptchaChanged,
        onLoginClick = { viewModel.submitLogin(onLoginSuccess) },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = onForgotPasswordClick
    )
}

@Suppress("FunctionName")
@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onNikOrEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onCaptchaChanged: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Wave inside single scrollable Column (zero touch interception issues!)
        RTCurvedHeader(
            height = 268.dp,
            bottomCornerRadius = 36.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RuangTungguLogoCard(
                    cardSize = 68.dp,
                    cornerRadius = 18.dp,
                    showOuterHalo = false
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Selamat datang kembali",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Masuk untuk mulai antre tanpa menunggu",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = RTColors.PureWhite.copy(alpha = 0.92f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-44).dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RTFloatingCard(
                cornerRadius = 22.dp,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp)
            ) {
                RTTextField(
                    label = "NIK / Email",
                    value = uiState.nikOrEmail,
                    onValueChange = onNikOrEmailChanged,
                    placeholder = "Masukkan NIK atau email",
                    leadingIcon = Icons.Outlined.Badge,
                    isValidCheck = uiState.isIdentifierValid,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(14.dp))

                RTTextField(
                    label = "Kata sandi",
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
                    placeholder = "Masukkan kata sandi",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    isError = uiState.errorMessage != null,
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Cloudflare Turnstile Widget
                CloudflareTurnstileCaptcha(
                    checked = uiState.captchaChecked,
                    onVerified = { _ -> onCaptchaChanged(true) },
                    onReset = { onCaptchaChanged(false) }
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(RTColors.ErrorSurface)
                            .border(1.dp, RTColors.ErrorRed, RoundedCornerShape(11.dp))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(RTColors.ErrorRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PriorityHigh,
                                contentDescription = "Peringatan",
                                tint = RTColors.PureWhite,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = uiState.errorMessage,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RTColors.ErrorRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                RTPrimaryButton(
                    text = "Login",
                    onClick = onLoginClick,
                    isLoading = uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lupa kata sandi?",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.BrandPrimaryTeal,
                        modifier = Modifier
                            .springClickable(onClick = onForgotPasswordClick)
                            .padding(vertical = 6.dp, horizontal = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            val registerInteraction = remember { MutableInteractionSource() }
            Text(
                text = "Belum punya akun? Daftar sekarang",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.BrandPrimaryTeal,
                modifier = Modifier
                    .tactilePressScale(registerInteraction)
                    .clickable(
                        interactionSource = registerInteraction,
                        indication = null,
                        onClick = onNavigateToRegister
                    )
                    .padding(vertical = 6.dp, horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Dengan masuk, Anda menyetujui Syarat & Ketentuan",
                fontSize = 11.sp,
                color = RTColors.MutedSlate,
                textAlign = TextAlign.Center
            )
        }
    }
}
