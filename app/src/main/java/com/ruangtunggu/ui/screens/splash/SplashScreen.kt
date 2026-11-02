package com.ruangtunggu.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.Dimens

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val destination by viewModel.destination.collectAsState()

    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.ONBOARDING -> {
                navController.navigate(Routes.Onboarding) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
            SplashDestination.LOGIN -> {
                navController.navigate(Routes.Login) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
            SplashDestination.HOME -> {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
            SplashDestination.LOADING -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00897B), // Teal
                        Color(0xFF004D40)  // Dark Teal
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(Dimens.PaddingScreen)
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // App Logo Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Logo RuangTunggu",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingXl))

            // App Name
            Text(
                text = "RuangTunggu",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSm))

            // Tagline from Figma
            Text(
                text = "Antre tanpa ribet, sehat tanpa menunggu",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

