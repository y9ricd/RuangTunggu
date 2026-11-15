package com.ruangtunggu.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Pixel-Perfect Splash Screen with 3-Phase Cinematic Entrance Animation:
 * - Phase 1 (0–300ms): Wave header fades in from top (slide down -40→0dp + fade 0→1)
 * - Phase 2 (300–650ms): Logo scale spring bounce (0.6→1.0) + subtitle fade-in stagger
 * - Phase 3 (650–900ms): Bottom content slides up from +60dp → 0dp, fades in
 * - Infinite: Ambient floating pulse on logo halo (scale 0.95↔1.05, 2800ms ease-in-out loop)
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
    // ── Animatable states ──────────────────────────────────────────────
    val headerAlpha    = remember { Animatable(0f) }
    val headerOffsetY  = remember { Animatable(-40f) }
    val logoScale      = remember { Animatable(0.60f) }
    val logoAlpha      = remember { Animatable(0f) }
    val titleAlpha     = remember { Animatable(0f) }
    val subtitleAlpha  = remember { Animatable(0f) }
    val bottomAlpha    = remember { Animatable(0f) }
    val bottomOffsetY  = remember { Animatable(60f) }
    val buttonScale    = remember { Animatable(0.88f) }

    // Phase driver
    LaunchedEffect(Unit) {
        // Phase 1 — header reveal
        launch {
            headerAlpha.animateTo(1f, tween(380, easing = FastOutSlowInEasing))
        }
        launch {
            headerOffsetY.animateTo(0f, tween(380, easing = FastOutSlowInEasing))
        }

        delay(220)

        // Phase 2 — logo spring + text stagger
        launch {
            logoAlpha.animateTo(1f, tween(300))
            logoScale.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)
            )
        }

        delay(180)

        launch { titleAlpha.animateTo(1f, tween(300)) }
        delay(100)
        launch { subtitleAlpha.animateTo(1f, tween(280)) }

        delay(260)

        // Phase 3 — bottom content slide-up
        launch {
            bottomAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }
        launch {
            bottomOffsetY.animateTo(
                0f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
            )
        }

        delay(120)

        // Button micro-bounce
        launch {
            buttonScale.animateTo(1.05f, tween(180))
            buttonScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    // Ambient floating logo pulse (infinite, subtle)
    val infiniteTransition = rememberInfiniteTransition(label = "LogoPulse")
    val logoPulse by infiniteTransition.animateFloat(
        initialValue = 1.00f,
        targetValue  = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "LogoAmbientPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RTColors.PureWhite)
    ) {
        // ── Top ~52%: Teal Wave Header ────────────────────────────────
        RTSplashWaveHeader(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.53f)
                .graphicsLayer {
                    alpha = headerAlpha.value
                    translationY = headerOffsetY.value.dp.toPx()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo — spring bounce + ambient pulse
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = logoScale.value * logoPulse
                            scaleY = logoScale.value * logoPulse
                            alpha  = logoAlpha.value
                        }
                ) {
                    RuangTungguLogoCard(
                        cardSize      = 92.dp,
                        cornerRadius  = 24.dp,
                        showOuterHalo = true
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // App title — fade-in
                Text(
                    text          = uiState.appTitle,
                    fontSize      = 26.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.6.sp,
                    color         = RTColors.PureWhite,
                    textAlign     = TextAlign.Center,
                    modifier      = Modifier.alpha(titleAlpha.value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle — staggered after title
                Text(
                    text       = uiState.appSubtitle,
                    fontSize   = 13.5.sp,
                    fontWeight = FontWeight.Normal,
                    color      = RTColors.PureWhite.copy(alpha = 0.92f),
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.alpha(subtitleAlpha.value)
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        // ── Bottom ~48%: White CTA Area — slide-up reveal ─────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.47f)
                .background(RTColors.PureWhite)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .graphicsLayer {
                    alpha       = bottomAlpha.value
                    translationY = bottomOffsetY.value.dp.toPx()
                },
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
                    text       = uiState.heroHeading,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = RTColors.NavyHeading,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text       = uiState.heroDescription,
                    fontSize   = 13.5.sp,
                    lineHeight = 22.sp,
                    color      = RTColors.SlateBodyText,
                    textAlign  = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "Mulai" button — micro-bounce on first draw
                Box(modifier = Modifier.scale(buttonScale.value)) {
                    RTPrimaryButton(
                        text    = uiState.primaryButtonText,
                        onClick = onMulaiClick
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                val linkInteraction = remember { MutableInteractionSource() }
                Text(
                    text       = uiState.secondaryLinkText,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = RTColors.BrandPrimaryTeal,
                    modifier   = Modifier
                        .tactilePressScale(linkInteraction)
                        .clickable(
                            interactionSource = linkInteraction,
                            indication        = null,
                            onClick           = onMasukClick
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
            uiState      = SplashUiState(),
            onMulaiClick = {},
            onMasukClick = {}
        )
    }
}
