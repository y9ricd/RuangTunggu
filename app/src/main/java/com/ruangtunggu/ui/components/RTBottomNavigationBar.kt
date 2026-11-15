package com.ruangtunggu.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ruangtunggu.ui.theme.RTColors

enum class HomeTab(
    val title: String,
    val icon: ImageVector
) {
    BERANDA("Beranda", Icons.Outlined.Home),
    ANTREAN("Antrean", Icons.AutoMirrored.Outlined.Assignment),
    RIWAYAT("Riwayat", Icons.Outlined.Schedule),
    PROFIL("Profil", Icons.Outlined.PersonOutline)
}

/**
 * Custom Bottom Navigation Bar matching Beranda.png, Pantau antrean.png, Riwayat_Aktif 1.png, Profil.png:
 * - Active capsule indicator in Mint (#D1F4EC) with #0A8F82 icon & bold label
 * - Inactive icons and labels in Muted Slate (#94A3B8)
 */
@Suppress("FunctionName")
@Composable
fun RTBottomNavigationBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                spotColor = RTColors.CardSpotShadow,
                ambientColor = RTColors.CardAmbientShadow
            ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = RTColors.PureWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val interactionSource = remember { MutableInteractionSource() }

                val capsuleBg by animateColorAsState(
                    targetValue = if (isSelected) RTColors.MintSurfaceActive else Color.Transparent,
                    animationSpec = tween(220),
                    label = "TabCapsuleBg"
                )
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.SlateBodyText,
                    animationSpec = tween(220),
                    label = "TabIconTint"
                )
                val textTint by animateColorAsState(
                    targetValue = if (isSelected) RTColors.BrandPrimaryTeal else RTColors.MutedSlate,
                    animationSpec = tween(220),
                    label = "TabTextTint"
                )
                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.08f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "TabIconScale"
                )

                Column(
                    modifier = Modifier
                        .tactilePressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onTabSelected(tab) }
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(34.dp)
                            .clip(RoundedCornerShape(17.dp))
                            .background(capsuleBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = iconTint,
                            modifier = Modifier
                                .size(22.dp)
                                .scale(iconScale)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tab.title,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = textTint
                    )
                }
            }
        }
    }
}
