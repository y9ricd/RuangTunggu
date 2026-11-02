package com.ruangtunggu.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruangtunggu.ui.theme.RTColors

@Suppress("FunctionName")
@Composable
fun RTSplashWaveHeader(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    val waveProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.35f,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "OrganicWaveParallax"
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val waveAmplitude = 38.dp.toPx() * waveProgress
            val baselineY = h - 36.dp.toPx()

            val wavePath = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, baselineY)
                cubicTo(
                    w * 0.25f, baselineY - waveAmplitude * 1.35f,
                    w * 0.62f, baselineY + waveAmplitude * 1.45f,
                    w, baselineY - waveAmplitude * 0.55f
                )
                lineTo(w, 0f)
                close()
            }

            drawPath(
                path = wavePath,
                brush = RTColors.HeaderWaveGradient
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = w * 0.36f * waveProgress,
                center = Offset(w * 0.92f, h * 0.08f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.055f),
                radius = w * 0.28f * waveProgress,
                center = Offset(w * 0.02f, h * 0.72f)
            )
        }

        content()
    }
}

@Suppress("FunctionName")
@Composable
fun RTCurvedHeader(
    height: Dp,
    modifier: Modifier = Modifier,
    bottomCornerRadius: Dp = 32.dp,
    content: @Composable BoxScope.() -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val parallaxOffset by animateFloatAsState(
        targetValue = if (entered) 1f else 0.6f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "HeaderBubbleParallax"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(
                RoundedCornerShape(
                    bottomStart = bottomCornerRadius,
                    bottomEnd = bottomCornerRadius
                )
            )
            .background(brush = RTColors.HeaderWaveGradient)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = w * 0.34f * parallaxOffset,
                center = Offset(w * 0.90f, h * 0.14f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.055f),
                radius = w * 0.24f * parallaxOffset,
                center = Offset(w * 0.04f, h * 0.76f)
            )
        }

        content()
    }
}

@Suppress("FunctionName")
@Composable
fun RTHeaderBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .tactilePressScale()
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Kembali",
            tint = RTColors.PureWhite,
            modifier = Modifier.size(26.dp)
        )
    }
}

/**
 * Design System Inheritance Rule #2:
 * Clean white TopAppBar with 18sp Navy Bold title and Teal back arrow for detail list screens.
 */
@Suppress("FunctionName")
@Composable
fun RTDetailTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingAction: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = RTColors.PureWhite,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(58.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = RTColors.BrandPrimaryTeal
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RTColors.NavyHeading,
                modifier = Modifier.weight(1f)
            )
            if (trailingAction != null) {
                trailingAction()
            }
        }
    }
}
