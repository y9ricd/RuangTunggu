package com.ruangtunggu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ruangtunggu.R

/**
 * Squircle White Logo Card as seen in Figma splash & login screens.
 * Contains the official RuangTunggu healthcare orbit logo.
 */
@Composable
fun RTLogoCard(
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = Color(0x33000000),
                ambientColor = Color(0x1F000000)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_ruangtunggu),
            contentDescription = "Logo RuangTunggu",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit
        )
    }
}

