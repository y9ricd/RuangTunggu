package com.ruangtunggu.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.CompletedConsultation
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.ui.theme.RTColors
import kotlinx.coroutines.delay

/**
 * 3. "Spring Touch Feedback" (Sentuhan Lembut Medis):
 * Scales down to 0.96f when pressed and rebounds to 1.0f with soft elastic spring damping.
 */
fun Modifier.tactilePressScale(
    interactionSource: MutableInteractionSource? = null,
    pressedScale: Float = 0.96f
): Modifier = composed {
    val internalInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by internalInteractionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SpringTouchFeedback"
    )
    this.scale(scale)
}

fun Modifier.springClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this
        .tactilePressScale(interactionSource, pressedScale = 0.96f)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * 4. "Shimmer Loading Skeleton" (1200ms Brush.linearGradient gray-to-white)
 */
@Suppress("FunctionName")
@Composable
fun RTShimmerCardPlaceholder(
    modifier: Modifier = Modifier,
    height: Dp = 112.dp,
    cornerRadius: Dp = 20.dp
) {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerOffset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE2E8F0),
            Color(0xFFF8FAFC),
            Color(0xFFE2E8F0)
        ),
        start = Offset(translateAnim - 300f, 0f),
        end = Offset(translateAnim, 250f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush)
    )
}

/**
 * Design System Inheritance Rule #3:
 * Standardized Empty & Error State with pastel circular icon (#E0F7F4 or #F1F5F9),
 * 16sp Navy Bold title, and 13sp Slate informative description.
 */
@Suppress("FunctionName")
@Composable
fun RTEmptyStateView(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    circleColor: Color = RTColors.PastelTealCircle,
    iconTint: Color = RTColors.BrandPrimaryTeal,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = description,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = RTColors.SlateBodyText,
            textAlign = TextAlign.Center
        )

        if (actionButtonText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(18.dp))
            RTPrimaryButton(
                text = actionButtonText,
                onClick = onActionClick
            )
        }
    }
}

/**
 * Realistic Lifecycle Rule #1:
 * Beranda Empty State Card when activeQueue == null (Fresh Start / New Account / Cancelled Queue).
 * - Soft Mint icon container (#D1F4EC) with medical calendar/ticket icon
 * - Title: "Belum Ada Antrean Hari Ini"
 * - Subtitle: "Cari rumah sakit atau pilih poli untuk mengambil nomor antrean dokter."
 * - CTA: "Daftar Antrean Sekarang" (navigates directly to Cari RS flow)
 */
@Suppress("FunctionName")
@Composable
fun RTBerandaEmptyQueueCard(
    onDaftarAntreanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RTFloatingCard(
        modifier = modifier,
        cornerRadius = 22.dp,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RTColors.MintSurfaceActive),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Belum Ada Antrean",
                    tint = RTColors.BrandPrimaryTeal,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Belum Ada Antrean Hari Ini",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cari rumah sakit atau pilih poli untuk mengambil nomor antrean dokter.",
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = RTColors.SlateBodyText
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        RTPrimaryButton(
            text = "Daftar Antrean Sekarang",
            onClick = onDaftarAntreanClick
        )
    }
}

/**
 * Post-Consultation Review Prompt Card on Beranda (matching Flow #6 & AS02_Beri_Penilaian)
 */
@Suppress("FunctionName")
@Composable
fun RTPostConsultationReviewCard(
    completed: CompletedConsultation,
    onBeriPenilaianClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RTFloatingCard(
        modifier = modifier,
        cornerRadius = 22.dp,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(RTColors.WarningAmberBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Penilaian",
                    tint = RTColors.StarGold,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pemeriksaan telah selesai",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.NavyHeading
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Berikan penilaian untuk ${completed.doctorName} (${completed.poliName})",
                    fontSize = 12.5.sp,
                    color = RTColors.SlateBodyText
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onDismissClick,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, RTColors.InputBorder),
                modifier = Modifier
                    .weight(0.4f)
                    .height(42.dp)
                    .tactilePressScale()
            ) {
                Text(
                    text = "Nanti",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RTColors.SlateBodyText
                )
            }

            Button(
                onClick = onBeriPenilaianClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RTColors.BrandPrimaryTeal),
                modifier = Modifier
                    .weight(0.6f)
                    .height(42.dp)
                    .tactilePressScale()
            ) {
                Text(
                    text = "Beri Penilaian ★",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite
                )
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun RTFloatingCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = RTColors.CardSpotShadow,
                ambientColor = RTColors.CardAmbientShadow
            ),
        shape = RoundedCornerShape(cornerRadius),
        color = RTColors.PureWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            content = content
        )
    }
}

@Suppress("FunctionName")
@Composable
fun RTTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    isValidCheck: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int? = null,
    digitsOnly: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val borderColor = when {
        isError -> RTColors.ErrorRed
        isValidCheck -> RTColors.BrandPrimaryTeal
        else -> RTColors.InputBorder
    }

    val backgroundColor = when {
        isError -> RTColors.ErrorSurface
        else -> RTColors.InputBackground
    }

    val leadingIconTint = when {
        isError -> RTColors.ErrorRed
        isValidCheck -> RTColors.BrandPrimaryTeal
        else -> RTColors.SlateBodyText
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = RTColors.NavyHeading,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = { raw ->
                val filtered = if (digitsOnly) raw.filter { it.isDigit() } else raw
                val finalValue = if (maxLength != null) filtered.take(maxLength) else filtered
                onValueChange(finalValue)
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = RTColors.NavyHeading
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            cursorBrush = SolidColor(RTColors.BrandPrimaryTeal),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .tactilePressScale(interactionSource)
                        .clip(RoundedCornerShape(13.dp))
                        .background(backgroundColor)
                        .border(1.dp, borderColor, RoundedCornerShape(13.dp))
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = label,
                        tint = leadingIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = RTColors.MutedSlate
                            )
                        }
                        innerTextField()
                    }
                    when {
                        isError -> {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(RTColors.ErrorRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PriorityHigh,
                                    contentDescription = "Error",
                                    tint = RTColors.PureWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        isValidCheck && !isPassword -> {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(RTColors.BrandPrimaryTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Valid",
                                    tint = RTColors.PureWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        isPassword -> {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Outlined.Visibility
                                    } else {
                                        Icons.Outlined.VisibilityOff
                                    },
                                    contentDescription = "Toggle kata sandi",
                                    tint = RTColors.MutedSlate,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Suppress("FunctionName")
@Composable
fun RTCaptchaBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val bgColor = if (checked) Color(0xFFECFDF5) else RTColors.InputBackground
    val borderColor = if (checked) RTColors.BrandPrimaryTeal else RTColors.InputBorder

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .tactilePressScale(interactionSource)
            .clip(RoundedCornerShape(13.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(13.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (checked) RTColors.BrandPrimaryTeal else RTColors.PureWhite)
                    .border(
                        width = 1.2.dp,
                        color = if (checked) RTColors.BrandPrimaryTeal else RTColors.InputBorder,
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Terverifikasi",
                        tint = RTColors.PureWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Saya bukan robot",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = RTColors.NavyHeading
            )
        }

        Text(
            text = "CAPTCHA",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = RTColors.MutedSlate
        )
    }
}

@Suppress("FunctionName")
@Composable
fun RTPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = RTColors.BrandPrimaryTeal
) {
    val interactionSource = remember { MutableInteractionSource() }
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = RTColors.PureWhite,
            disabledContainerColor = Color(0xFFE2E8F0),
            disabledContentColor = RTColors.MutedSlate
        ),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .tactilePressScale(interactionSource)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = RTColors.PureWhite,
                strokeWidth = 2.2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) RTColors.PureWhite else RTColors.MutedSlate
            )
        }
    }
}

/**
 * 4-Phase Hospital Queue Card (RTQueueCard) with:
 * - Phase 1 (MENUNGGU): Green badge ● Antrean aktif with 1400ms Breathing Pulse (scale 0.9f..1.2f, alpha 0.6f..1.0f)
 * - Phase 2 (PERINGATAN_DEKAT): Amber badge ● Giliran Anda Hampir Tiba + preparation prompt
 * - Phase 3 (DIPANGGIL): Solid Teal accent + 2x gentle Haptic Feedback + "Tunjukkan Tiket / QR Code"
 * - Phase 4 (DIJEDA): Amber pause indicator + emergency/visite explanation banner
 * - Dynamic Wait Time Formula: Sisa Antrean * (5..6 mins) -> "± 20–25 menit"
 */
@Suppress("FunctionName")
@Composable
fun RTQueueCard(
    queue: ActiveQueue,
    onPantauClick: () -> Unit,
    onShowQrTicketClick: () -> Unit,
    onAturPengingatClick: () -> Unit,
    onChatPetugasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    // Trigger 2x gentle Haptic Feedback when entering DIPANGGIL phase
    LaunchedEffect(queue.phase) {
        if (queue.phase == QueuePhase.DIPANGGIL) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(160)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    // 1. "Breathing Pulse" (Detak Relaksasi Medis: scale 0.9f..1.2f, alpha 0.6f..1.0f, 1400ms FastOutSlowInEasing)
    val infiniteTransition = rememberInfiniteTransition(label = "BreathingPulseTransition")
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingDotScale"
    )
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BreathingDotAlpha"
    )

    val isCalled = queue.phase == QueuePhase.DIPANGGIL
    val isNear = queue.phase == QueuePhase.PERINGATAN_DEKAT
    val isPaused = queue.phase == QueuePhase.DIJEDA

    val badgeBg = when (queue.phase) {
        QueuePhase.MENUNGGU -> RTColors.ActiveBadgeBg
        QueuePhase.PERINGATAN_DEKAT -> RTColors.WarningAmberBg
        QueuePhase.DIPANGGIL -> RTColors.MintSurfaceActive
        QueuePhase.DIJEDA -> RTColors.PausedBadgeBg
    }
    val badgeDotColor = when (queue.phase) {
        QueuePhase.MENUNGGU -> RTColors.ActiveBadgeDot
        QueuePhase.PERINGATAN_DEKAT -> RTColors.WarningAmberBorder
        QueuePhase.DIPANGGIL -> RTColors.BrandPrimaryTeal
        QueuePhase.DIJEDA -> RTColors.PausedAmber
    }
    val badgeTextColor = when (queue.phase) {
        QueuePhase.MENUNGGU -> RTColors.ActiveBadgeText
        QueuePhase.PERINGATAN_DEKAT -> RTColors.WarningAmberText
        QueuePhase.DIPANGGIL -> RTColors.BrandDarkTeal
        QueuePhase.DIJEDA -> RTColors.WarningAmberText
    }
    val badgeLabel = when (queue.phase) {
        QueuePhase.MENUNGGU -> "Antrean aktif"
        QueuePhase.PERINGATAN_DEKAT -> "Giliran Anda Hampir Tiba"
        QueuePhase.DIPANGGIL -> "Giliran Anda Dipanggil!"
        QueuePhase.DIJEDA -> "Antrean Dijeda Sementara"
    }

    RTFloatingCard(
        modifier = modifier.then(
            if (isCalled) {
                Modifier.border(2.dp, RTColors.BrandPrimaryTeal, RoundedCornerShape(22.dp))
            } else if (isNear || isPaused) {
                Modifier.border(1.2.dp, RTColors.WarningAmberBorder, RoundedCornerShape(22.dp))
            } else {
                Modifier
            }
        ),
        cornerRadius = 22.dp,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // Row 1: Breathing Pulse Status Badge + Date/Time
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(badgeBg)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dotScale)
                        .clip(CircleShape)
                        .background(badgeDotColor.copy(alpha = dotAlpha))
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = badgeLabel,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeTextColor
                )
            }

            Text(
                text = queue.scheduleDateTime,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = RTColors.MutedSlate
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 2: 2. "Fluid Number Flip" (AnimatedContent with slideInVertically + fadeIn)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(
                targetState = queue.userQueueNumber,
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut()
                    ).using(SizeTransform(clip = false))
                },
                label = "UserQueueNumberFlip"
            ) { number ->
                Text(
                    text = number,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isPaused) RTColors.PausedAmber else RTColors.BrandPrimaryTeal,
                    letterSpacing = 0.5.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isPaused) "Dilayani (dijeda)" else "Sedang dilayani",
                    fontSize = 11.sp,
                    color = RTColors.MutedSlate
                )
                Spacer(modifier = Modifier.height(2.dp))
                AnimatedContent(
                    targetState = queue.currentServedNumber,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut()
                        ).using(SizeTransform(clip = false))
                    },
                    label = "CurrentServedFlip"
                ) { served ->
                    Text(
                        text = served,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RTColors.NavyHeading
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${queue.poliName} · ${queue.hospitalName}",
            fontSize = 13.5.sp,
            color = RTColors.SlateBodyText
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Bar (Teal or Amber when paused)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(RTColors.InputBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = queue.progress.coerceIn(0.1f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isPaused) RTColors.WarningAmberBorder else RTColors.BrandPrimaryTeal
                    )
            )
        }

        // Contextual State Machine Banner (Phase 2 Near, Phase 3 Called, Phase 4 Paused)
        if (isNear) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RTColors.WarningAmberBg)
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = RTColors.WarningAmberText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Silakan bersiap di ruang tunggu depan ${queue.poliName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RTColors.WarningAmberText
                )
            }
        } else if (isPaused) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RTColors.WarningAmberBg)
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PauseCircleFilled,
                    contentDescription = null,
                    tint = RTColors.WarningAmberText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pelayanan poli dijeda sementara (Est. 15 menit). Dokter sedang visite/tindakan darurat.",
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RTColors.WarningAmberText
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isCalled) {
            // Phase 3: Prominent Solid Teal Button "Tunjukkan Tiket / QR Code"
            Button(
                onClick = onShowQrTicketClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RTColors.BrandPrimaryTeal),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .tactilePressScale()
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = null,
                    tint = RTColors.PureWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tunjukkan Tiket / QR Code",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RTColors.PureWhite
                )
            }
        } else {
            // Standard Footer with Dynamic Wait Time Formula ("Sisa 4 antrean · ± 20–25 menit")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(
                    targetState = queue.dynamicWaitSummary,
                    transitionSpec = {
                        (slideInVertically { h -> h } + fadeIn()).togetherWith(
                            slideOutVertically { h -> -h } + fadeOut()
                        )
                    },
                    label = "DynamicWaitTimeSlide"
                ) { waitSummary ->
                    Text(
                        text = waitSummary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = RTColors.SlateBodyText,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isPaused) {
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .springClickable(onClick = onChatPetugasClick)
                                .clip(RoundedCornerShape(16.dp))
                                .background(RTColors.QuickActionCariRsBg)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Chat Petugas",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .springClickable(onClick = onAturPengingatClick)
                                .clip(RoundedCornerShape(16.dp))
                                .background(RTColors.QuickActionCariRsBg)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pengingat",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RTColors.BrandPrimaryTeal
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .springClickable(onClick = onPantauClick)
                            .clip(RoundedCornerShape(16.dp))
                            .background(RTColors.BrandPrimaryTeal)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pantau",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RTColors.PureWhite
                        )
                    }
                }
            }
        }
    }
}

/**
 * Deterministic Offline-First QR Code Canvas matching Pendaftaran berhasil.png & DL02_Tunjukkan_Tiket 1.png.
 * Works 100% offline from locally cached ticket payload without internet dependency.
 */
@Suppress("FunctionName")
@Composable
fun RTQrCodeCanvas(
    qrPayload: String,
    modifier: Modifier = Modifier,
    gridSize: Int = 15
) {
    Canvas(modifier = modifier) {
        val cellSize = size.width / gridSize
        val seed = qrPayload.hashCode()

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val isFinderTopLeft = row in 0..3 && col in 0..3
                val isFinderTopRight = row in 0..3 && col in (gridSize - 4) until gridSize
                val isFinderBottomLeft = row in (gridSize - 4) until gridSize && col in 0..3

                val shouldFill = when {
                    isFinderTopLeft || isFinderTopRight || isFinderBottomLeft -> {
                        val localR = if (row >= gridSize - 4) row - (gridSize - 4) else row
                        val localC = if (col >= gridSize - 4) col - (gridSize - 4) else col
                        localR == 0 || localR == 3 || localC == 0 || localC == 3 || (localR in 1..2 && localC in 1..2)
                    }
                    else -> {
                        val hash = ((row * 31 + col * 17) xor seed) and 0xFF
                        hash % 3 != 0
                    }
                }

                if (shouldFill) {
                    drawRoundRect(
                        color = RTColors.NavyHeading,
                        topLeft = Offset(col * cellSize + 1f, row * cellSize + 1f),
                        size = Size(cellSize - 2f, cellSize - 2f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
                    )
                }
            }
        }
    }
}
