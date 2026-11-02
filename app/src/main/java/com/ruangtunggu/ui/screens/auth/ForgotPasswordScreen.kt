package com.ruangtunggu.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTHeaderWave
import com.ruangtunggu.ui.components.RTTextField
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentStep by remember { mutableIntStateOf(1) }
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Top Wave Header ─────────────────────────────────────────
            RTHeaderWave(
                modifier = Modifier.fillMaxWidth(),
                height = 220.dp,
                waveDip = 0.86f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f))
                                .clickable {
                                    if (currentStep > 1) currentStep-- else navController.popBackStack()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Lupa Kata Sandi",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Langkah $currentStep dari 4",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }
                }
            }

            // ── Floating White Form Card ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0x1F000000),
                        ambientColor = Color(0x12000000)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (currentStep) {
                        1 -> {
                            Text(
                                text = "Masukkan email akun Anda untuk menerima kode OTP pemulihan kata sandi.",
                                fontSize = 13.sp,
                                color = RTBrandSlate,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            RTTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email Terdaftar",
                                placeholder = "contoh@email.com",
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = RTBrandSlate)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (email.isBlank()) {
                                        scope.launch { snackbarHostState.showSnackbar("Masukkan email Anda") }
                                        return@Button
                                    }
                                    isLoading = true
                                    scope.launch {
                                        delay(800)
                                        isLoading = false
                                        currentStep = 2
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                            ) {
                                Text("Kirim Kode OTP", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        2 -> {
                            Text(
                                text = "Kode verifikasi 6 digit telah dikirimkan ke $email.",
                                fontSize = 13.sp,
                                color = RTBrandSlate,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            RTTextField(
                                value = otp,
                                onValueChange = { if (it.length <= 6) otp = it },
                                label = "Kode OTP (6 digit)",
                                placeholder = "123456",
                                leadingIcon = {
                                    Icon(Icons.Default.Pin, contentDescription = null, tint = RTBrandSlate)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (otp.length < 6) {
                                        scope.launch { snackbarHostState.showSnackbar("Kode OTP harus 6 digit") }
                                        return@Button
                                    }
                                    currentStep = 3
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                            ) {
                                Text("Verifikasi OTP", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        3 -> {
                            Text(
                                text = "Buat kata sandi baru yang kuat minimal 8 karakter.",
                                fontSize = 13.sp,
                                color = RTBrandSlate,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            RTTextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = "Kata Sandi Baru",
                                placeholder = "Minimal 8 karakter",
                                isPassword = true
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            RTTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = "Konfirmasi Kata Sandi",
                                placeholder = "Ulangi kata sandi baru",
                                isPassword = true
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (newPassword.length < 8) {
                                        scope.launch { snackbarHostState.showSnackbar("Kata sandi minimal 8 karakter") }
                                        return@Button
                                    }
                                    if (newPassword != confirmPassword) {
                                        scope.launch { snackbarHostState.showSnackbar("Kata sandi tidak cocok") }
                                        return@Button
                                    }
                                    currentStep = 4
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                            ) {
                                Text("Simpan Kata Sandi", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        4 -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = RTBrandTeal,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Kata Sandi Berhasil Diperbarui!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTBrandNavy,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Silakan masuk kembali menggunakan kata sandi baru Anda.",
                                fontSize = 13.sp,
                                color = RTBrandSlate,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    navController.navigate(Routes.Login) {
                                        popUpTo(Routes.ForgotPassword) { inclusive = true }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                            ) {
                                Text("Masuk Sekarang", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
