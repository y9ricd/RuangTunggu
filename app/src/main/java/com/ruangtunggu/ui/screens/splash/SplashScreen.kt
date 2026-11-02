package com.ruangtunggu.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ruangtunggu.ui.components.RTPrimaryButton
import com.ruangtunggu.ui.components.RTSplashWaveHeader
import com.ruangtunggu.ui.components.RuangTungguLogoCard
import com.ruangtunggu.ui.components.tactilePressScale
import com.ruangtunggu.ui.theme.RTColors
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Pixel-Perfect Splash Screen matching 01. Splashscreen.png:
 * - Top (~52% height): Teal gradient wave header with organic Cubic Bezier curve & ambient bubble glow,
 *   92dp white squircle card (24dp corner, outer translucent squircle halo) containing RuangTunggu Logo,
 *   "RuangTunggu" (White, Bold, 26sp, letterSpacing 1.6sp),
 *   "Antre tanpa ribet, sehat tanpa menunggu" (White alpha 0.92, 13.5sp, centered).
 * - Bottom (Pure White background):
 *   "Antre dari mana saja" (Navy #0F1E36, Bold, 22sp, centered),
 *   "Daftar, pantau, dan dapatkan notifikasi
giliran Anda langsung dari ponsel.” (Slate #64748B, 13.5sp, lineHeight 22sp),
 *   "Mulai" button (Solid Teal #0A8F82, height 50dp, rounded 14dp),
 *   "Sudah punya akun? Masuk" link (Teal #0A8F82, SemiBold, 14sp).
 */
@Suppress("FunctionName")
@Composable
fun SplashScreen(
    onMulaiClick: () -> Unit,
    onMasukClick: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SplashScreenContent(
        uiState = uiState,
        onMulaiClick = onMulaiClick,
        onMasukClick = onMasukClick
    )
}

@Suppress("FunctionName")
@Composable
fun SplashScreenContent(
    uiState: SplashUiState,
    onMulaiClick: () -> Unit,
    onMasukClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.PureWhite)
    ) {
        // Top ~52% Height: Organic Wave Header + Logo + Brand Title
        RTSplashWaveHeader(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.53f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RuangTungguLogoCard(
                    cardSize = 92.dp,
                    cornerRadius = 24.dp,
                    showOuterHalo = true
                )

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = uiState.appTitle,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp,
                    color = RTColors.PureWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.appSubtitle,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = RTColors.PureWhite.copy(alpha = 0.92f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        // Bottom ~48% Height: White Canvas Content + CTA Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.47f)
                .background(RTColors.PureWhite)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 34.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.heroHeading,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.heroDescription,
                    fontSize = 13.5.sp,
                    lineHeight = 22.sp,
                    color = RTColors.SlateBodyText,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RTPrimaryButton(
                    text = uiState.primaryButtonText,
                    onClick = onMulaiClick
                )

                Spacer(modifier = Modifier.height(18.dp))

                val linkInteraction = remember { MutableInteractionSource() }
                Text(
                    text = uiState.secondaryLinkText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RTColors.BrandPrimaryTeal,
                    modifier = Modifier
                        .tactilePressScale(linkInteraction)
                        .clickable(
                            interactionSource = linkInteraction,
                            indication = null,
                            onClick = onMasukClick
                        )
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    RuangTungguTheme {
        SplashScreenContent(
            uiState = SplashUiState(),
            onMulaiClick = {},
            onMasukClick = {}
        )
    }
}
