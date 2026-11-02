package com.ruangtunggu.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ruangtunggu.ui.theme.Dimens
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Modifier extension that applies an animated shimmering gradient placeholder.
 */
fun Modifier.shimmerEffect(
    shape: Shape = RoundedCornerShape(Dimens.SpacingSm)
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )

    val isDark = MaterialTheme.colorScheme.background.run {
        (red * 0.299 + green * 0.587 + blue * 0.114) < 0.5
    }

    val baseColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFE0E0E0)
    val highlightColor = if (isDark) Color(0xFF3D3D3D) else Color(0xFFF5F5F5)

    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnim.value - 200f, translateAnim.value - 200f),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    this
        .clip(shape)
        .background(brush)
}

/**
 * Skeleton placeholder for text lines.
 */
@Composable
fun RTSkeletonLine(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(Dimens.SpacingXs)
) {
    Box(
        modifier = modifier
            .height(height)
            .shimmerEffect(shape)
    )
}

/**
 * Circular skeleton placeholder (e.g. for avatars and round icons).
 */
@Composable
fun RTSkeletonCircle(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .shimmerEffect(CircleShape)
    )
}

/**
 * Card skeleton placeholder for loading lists and dashboards.
 */
@Composable
fun RTSkeletonCard(
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    shape: Shape = RoundedCornerShape(Dimens.SpacingLg)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shimmerEffect(shape)
    )
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "Loading Skeleton (Light)", showBackground = true)
@Composable
private fun RTSkeletonPreviewLight() {
    RuangTungguTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            RTSkeletonCard()
            Spacer(modifier = Modifier.height(16.dp))
            RTSkeletonLine(modifier = Modifier.fillMaxWidth(0.7f))
            Spacer(modifier = Modifier.height(8.dp))
            RTSkeletonLine(modifier = Modifier.fillMaxWidth(0.4f))
            Spacer(modifier = Modifier.height(16.dp))
            RTSkeletonCircle(size = 56.dp)
        }
    }
}

@Preview(name = "Loading Skeleton (Dark)", showBackground = true)
@Composable
private fun RTSkeletonPreviewDark() {
    RuangTungguTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            RTSkeletonCard()
            Spacer(modifier = Modifier.height(16.dp))
            RTSkeletonLine(modifier = Modifier.fillMaxWidth(0.7f))
            Spacer(modifier = Modifier.height(8.dp))
            RTSkeletonLine(modifier = Modifier.fillMaxWidth(0.4f))
            Spacer(modifier = Modifier.height(16.dp))
            RTSkeletonCircle(size = 56.dp)
        }
    }
}

