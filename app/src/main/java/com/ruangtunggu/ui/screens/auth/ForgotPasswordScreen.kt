package com.ruangtunggu.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTButton
import com.ruangtunggu.ui.components.RTCard
import com.ruangtunggu.ui.components.RTTextField
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Steps: 1 = Email, 2 = OTP, 3 = New Password, 4 = Success
    var currentStep by remember { mutableIntStateOf(1) }
    var email by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (currentStep) {
                            1 -> "Lupa Kata Sandi"
                            2 -> "Verifikasi Kode"
                            3 -> "Atur Sandi Baru"
                            else -> "Berhasil"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    if (currentStep < 4) {
                        IconButton(onClick = {
                            if (currentStep > 1) currentStep-- else navController.popBackStack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.PaddingScreen),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentStep) {
                1 -> {
                    // Step 1: Input Email
                    Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                    Text(
                        text = "Masukkan alamat email yang terdaftar untuk menerima kode verifikasi pemulihan sandi.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXl))

                    RTCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(Dimens.SpacingLg)) {
                            RTTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Alamat Email",
                                placeholder = "nama@email.com",
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
                            )
                            Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                            RTButton(
                                text = "Kirim Kode Verifikasi",
                                onClick = {
                                    if (email.isBlank()) {
                                        scope.launch { snackbarHostState.showSnackbar("Email tidak boleh kosong") }
                                    } else {
                                        isLoading = true
                                        scope.launch {
                                            delay(600)
                                            isLoading = false
                                            currentStep = 2
                                        }
                                    }
                                },
                                isLoading = isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                2 -> {
                    // Step 2: Input OTP
                    Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                    Text(
                        text = "Kode verifikasi telah dikirim ke $email. Masukkan 4 digit kode tersebut di bawah ini.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXl))

                    RTCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(Dimens.SpacingLg)) {
                            RTTextField(
                                value = otpCode,
                                onValueChange = { if (it.length <= 4) otpCode = it },
                                label = "Kode OTP",
                                placeholder = "Contoh: 1234",
                                leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) }
                            )
                            Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                            RTButton(
                                text = "Verifikasi Kode",
                                onClick = {
                                    if (otpCode.length < 4) {
                                        scope.launch { snackbarHostState.showSnackbar("Masukkan 4 digit kode OTP") }
                                    } else {
                                        isLoading = true
                                        scope.launch {
                                            delay(500)
                                            isLoading = false
                                            currentStep = 3
                                        }
                                    }
                                },
                                isLoading = isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                3 -> {
                    // Step 3: Input New Password
                    Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                    Text(
                        text = "Buat kata sandi baru untuk akun Anda. Pastikan sandi mudah diingat namun aman.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXl))

                    RTCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(Dimens.SpacingLg)) {
                            RTTextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = "Kata Sandi Baru",
                                placeholder = "Minimal 6 karakter",
                                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null) }
                            )
                            Spacer(modifier = Modifier.height(Dimens.SpacingLg))
                            RTButton(
                                text = "Simpan Kata Sandi",
                                onClick = {
                                    if (newPassword.length < 6) {
                                        scope.launch { snackbarHostState.showSnackbar("Sandi minimal 6 karakter") }
                                    } else {
                                        isLoading = true
                                        scope.launch {
                                            delay(600)
                                            isLoading = false
                                            currentStep = 4
                                        }
                                    }
                                },
                                isLoading = isLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                4 -> {
                    // Step 4: Success
                    Spacer(modifier = Modifier.height(Dimens.SpacingXxl))
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.SpacingLg))

                    Text(
                        text = "Kata Sandi Berhasil Diubah",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingSm))

                    Text(
                        text = "Silakan masuk kembali dengan menggunakan kata sandi baru Anda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingXxl))

                    RTButton(
                        text = "Kembali ke Login",
                        onClick = {
                            navController.navigate(Routes.Login) {
                                popUpTo(Routes.ForgotPassword) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

