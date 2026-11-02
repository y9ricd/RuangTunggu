package com.ruangtunggu.ui.screens.onboarding

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.RTBrandBorder
import com.ruangtunggu.ui.theme.RTBrandInputBg
import com.ruangtunggu.ui.theme.RTBrandMuted
import com.ruangtunggu.ui.theme.RTBrandNavy
import com.ruangtunggu.ui.theme.RTBrandSlate
import com.ruangtunggu.ui.theme.RTBrandTeal
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    fun navigateToLogin() {
        viewModel.completeOnboarding()
        navController.navigate(Routes.Login) {
            popUpTo(Routes.Onboarding) { inclusive = true }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Lewati button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (pagerState.currentPage < 2) {
                    TextButton(onClick = { navigateToLogin() }) {
                        Text(
                            text = "Lewati",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RTBrandSlate
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingSlide1()
                    1 -> OnboardingSlide2()
                    2 -> OnboardingSlide3()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dots Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) RTBrandTeal else RTBrandBorder)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bottom Buttons
            if (pagerState.currentPage < 2) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                ) {
                    Text(
                        text = "Lanjut",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Button(
                    onClick = { navigateToLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RTBrandTeal)
                ) {
                    Text(
                        text = "Mulai",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Sudah punya akun? Masuk",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RTBrandTeal,
                    modifier = Modifier
                        .clickable { navigateToLogin() }
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// ── Slide 1: Cari Dokter (Figma 02. Splashscreen 1) ─────────────────
@Composable
private fun OnboardingSlide1() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Visual Mockup Card
        Box(
            modifier = Modifier
                .size(240.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0x1F000000))
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFE6F7F5))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, RTBrandBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = RTBrandTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RS Sehat Sidoarjo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Poli Anak · Umum · Gigi",
                    fontSize = 11.sp,
                    color = RTBrandSlate
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFDCFCE7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Tersedia 12 Jadwal",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Cari dokter dengan mudah",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RTBrandNavy,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Temukan rumah sakit, poli, dan jadwal dokter dalam satu aplikasi.",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RTBrandSlate,
            textAlign = TextAlign.Center
        )
    }
}

// ── Slide 2: Ambil Antrean (Figma 03. Splashscreen 2) ────────────────
@Composable
private fun OnboardingSlide2() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Visual Ticket Card
        Box(
            modifier = Modifier
                .size(240.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0x1F000000))
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFD1F4EC))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, RTBrandBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nomor Antrean Anda",
                    fontSize = 11.sp,
                    color = RTBrandSlate
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A-023",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RTBrandTeal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Poli Anak · dr. Abdul, Sp.A",
                    fontSize = 11.sp,
                    color = RTBrandNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Antre dari mana saja",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RTBrandNavy,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Daftar dan dapatkan nomor antrean tanpa harus datang lebih awal ke rumah sakit.",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RTBrandSlate,
            textAlign = TextAlign.Center
        )
    }
}

// ── Slide 3: Notifikasi Giliran (Figma 04. Splashscreen 3) ───────────
@Composable
private fun OnboardingSlide3() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Visual Notification Card
        Box(
            modifier = Modifier
                .size(240.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0x1F000000))
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFE8FAF8))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, RTBrandBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RTBrandTeal),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Giliran Anda Dekat!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RTBrandNavy
                    )
                    Text(
                        text = "Sisa 2 antrean lagi",
                        fontSize = 10.5.sp,
                        color = RTBrandSlate
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Dapat notifikasi giliran",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RTBrandNavy,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Kami kabari saat giliran Anda sudah dekat agar tidak membuang waktu menunggu lama.",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RTBrandSlate,
            textAlign = TextAlign.Center
        )
    }
}
