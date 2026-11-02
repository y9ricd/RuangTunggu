package com.ruangtunggu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ruangtunggu.R
import com.ruangtunggu.ui.theme.RTColors

/**
 * Official RuangTunggu Squircle Logo Card using 100% the user's uploaded
 * "Glossy Teal Healthcare Orbit Logo.png" asset (R.drawable.logo_ruangtunggu).
 */
@Suppress("FunctionName")
@Composable
fun RuangTungguLogoCard(
    modifier: Modifier = Modifier,
    cardSize: Dp = 92.dp,
    cornerRadius: Dp = 24.dp,
    showOuterHalo: Boolean = false
) {
    val haloSize = cardSize + 18.dp
    val haloCorner = cornerRadius + 5.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (showOuterHalo) {
            Box(
                modifier = Modifier
                    .size(haloSize)
                    .clip(RoundedCornerShape(haloCorner))
                    .background(RTColors.PureWhite.copy(alpha = 0.22f))
            )
        }

        Box(
            modifier = Modifier
                .size(cardSize)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    spotColor = RTColors.CardSpotShadow,
                    ambientColor = RTColors.CardAmbientShadow
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(RTColors.PureWhite)
                .padding(cardSize * 0.10f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_ruangtunggu),
                contentDescription = "Logo Resmi RuangTunggu",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
