package com.ruangtunggu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Signature RuangTunggu teal gradient with organic wave divider and ambient bubbles,
 * exactly matching the Figma design system.
 */
val RTTealGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F766E), // Deep Teal
        Color(0xFF0D9488), // Primary Brand Teal
        Color(0xFF14B8A6)  // Bright Teal Accent
    )
)

val RTTealBackground = Color(0xFF0D9488)

@Composable
fun RTHeaderWave(
    modifier: Modifier = Modifier,
    height: Dp = 320.dp,
    waveDip: Float = 0.88f,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // 1. Organic Wave Path
            val wavePath = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, h * waveDip)
                cubicTo(
                    x1 = w * 0.28f, y1 = h * (waveDip - 0.08f),
                    x2 = w * 0.65f, y2 = h * (waveDip + 0.09f),
                    x3 = w, y3 = h * waveDip
                )
                lineTo(w, 0f)
                close()
            }

            // Draw Gradient Header
            drawPath(
                path = wavePath,
                brush = RTTealGradient
            )

            // 2. Ambient Translucent Bubbles (Matching Figma preview)
            // Top Right Large Bubble
            drawCircle(
                color = Color.White.copy(alpha = 0.08f),
                radius = w * 0.42f,
                center = Offset(w * 0.95f, h * 0.12f)
            )

            // Middle Left Soft Glow Bubble
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = w * 0.28f,
                center = Offset(w * 0.08f, h * 0.45f)
            )

            // Bottom Right Subtle Ring
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = w * 0.20f,
                center = Offset(w * 0.85f, h * 0.70f)
            )
        }

        // Inner Composable Content
        content()
    }
}
