package com.ruangtunggu.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.ui.components.CloudflareTurnstileCaptcha
import com.ruangtunggu.ui.components.RTCurvedHeader
import com.ruangtunggu.ui.components.RTFloatingCard
import com.ruangtunggu.ui.components.RTHeaderBackButton
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTTextField
import com.ruangtunggu.ui.components.tactilePressScale
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFullNameChanged = viewModel::onFullNameChanged,
        onEmailOrPhoneChanged = viewModel::onEmailOrPhoneChanged,
        onNikChanged = viewModel::onNikChanged,
        onBirthPlaceDateChanged = viewModel::onBirthPlaceDateChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onCaptchaChanged = viewModel::onCaptchaChanged,
        onDaftarClick = { viewModel.submitRegistration(onRegisterSuccess) },
        onLoginClick = onLoginClick
    )
}

@Suppress("FunctionName")
@Composable
fun RegisterScreenContent(
    uiState: RegisterUiState,
    onBackClick: () -> Unit,
    onFullNameChanged: (String) -> Unit,
    onEmailOrPhoneChanged: (String) -> Unit,
    onNikChanged: (String) -> Unit,
    onBirthPlaceDateChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onCaptchaChanged: (Boolean) -> Unit,
    onDaftarClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.ScreenBackground)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Wave inside single Column so Back Button is 100% responsive on first tap!
        RTCurvedHeader(
            height = 168.dp,
            bottomCornerRadius = 32.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RTHeaderBackButton(onClick = onBackClick)

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Buat Akun Baru",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTColors.PureWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Lengkapi data diri Anda",
                        fontSize = 12.sp,
                        color = RTColors.PureWhite.copy(alpha = 0.92f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-38).dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RTFloatingCard(
                cornerRadius = 22.dp,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
            ) {
                RTTextField(
                    label = "Nama lengkap",
                    value = uiState.fullName,
                    onValueChange = onFullNameChanged,
                    placeholder = "Sesuai KTP",
                    leadingIcon = Icons.Outlined.PersonOutline,
                    isValidCheck = uiState.fullName.trim().length >= 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                RTTextField(
                    label = "Email / No. HP",
                    value = uiState.emailOrPhone,
                    onValueChange = onEmailOrPhoneChanged,
                    placeholder = "email@contoh.com",
                    leadingIcon = Icons.Outlined.Email,
                    keyboardType = KeyboardType.Email,
                    isValidCheck = uiState.emailOrPhone.length >= 6
                )

                Spacer(modifier = Modifier.height(12.dp))

                RTTextField(
                    label = "NIK",
                    value = uiState.nik,
                    onValueChange = onNikChanged,
                    placeholder = "16 digit NIK",
                    leadingIcon = Icons.Outlined.Badge,
                    keyboardType = KeyboardType.Number,
                    maxLength = 16,
                    digitsOnly = true,
                    isValidCheck = uiState.nik.length == 16
                )

                Spacer(modifier = Modifier.height(12.dp))

                RTTextField(
                    label = "Tempat, tanggal lahir",
                    value = uiState.birthPlaceDate,
                    onValueChange = onBirthPlaceDateChanged,
                    placeholder = "Kota, DD/MM/YYYY",
                    leadingIcon = Icons.Outlined.CalendarToday,
                    isValidCheck = uiState.birthPlaceDate.length >= 6
                )

                Spacer(modifier = Modifier.height(12.dp))

                RTTextField(
                    label = "Kata sandi",
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
                    placeholder = "Minimal 8 karakter",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(14.dp))

                CloudflareTurnstileCaptcha(
                    checked = uiState.captchaChecked,
                    onVerified = { _ -> onCaptchaChanged(true) },
                    onReset = { onCaptchaChanged(false) }
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.errorMessage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RTColors.ErrorRed
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                RTPrimaryButton(
                    text = "Daftar",
                    onClick = onDaftarClick,
                    isLoading = uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            val loginLinkInteraction = remember { MutableInteractionSource() }
            Text(
                text = "Sudah punya akun? Masuk",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.BrandPrimaryTeal,
                modifier = Modifier
                    .tactilePressScale(loginLinkInteraction)
                    .clickable(
                        interactionSource = loginLinkInteraction,
                        indication = null,
                        onClick = onLoginClick
                    )
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
    }
}
