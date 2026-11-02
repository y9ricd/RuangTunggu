package com.ruangtunggu.ui.screens.onboarding

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTButton
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.Dimens
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)

private val pages = listOf(
    OnboardingPage(
        title = "Pantau Antrean Real-time",
        description = "Pantau nomor antrean dokter Anda dari mana saja secara real-time tanpa perlu menunggu berjam-jam di rumah sakit.",
        icon = Icons.Default.AccessTime
    ),
    OnboardingPage(
        title = "Daftar Poli Lebih Cepat",
        description = "Pilih poliklinik dan jadwal dokter spesialis di berbagai rumah sakit terdekat langsung dari genggaman Anda.",
        icon = Icons.Default.LocalHospital
    ),
    OnboardingPage(
        title = "Pengingat Otomatis",
        description = "Dapatkan notifikasi cerdas saat giliran Anda sudah dekat, sehingga Anda bisa datang tepat pada waktunya.",
        icon = Icons.Default.NotificationsActive
    )
)

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    fun navigateToLogin() {
        viewModel.completeOnboarding()
        navController.navigate(Routes.Login) {
            popUpTo(Routes.Onboarding) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingScreen)
    ) {
        // Skip button at Top Right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navigateToLogin() }) {
                Text(
                    text = "Lewati",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Pager Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.SpacingLg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingXxl))

                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingMd))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Pager Indicators & Action Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dot Indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingXl))

            // Action Button
            val isLastPage = pagerState.currentPage == pages.size - 1
            RTButton(
                text = if (isLastPage) "Mulai Sekarang" else "Lanjutkan",
                onClick = {
                    if (isLastPage) {
                        navigateToLogin()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMd))
        }
    }
}

