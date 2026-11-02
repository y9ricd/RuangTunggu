package com.ruangtunggu.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val RuangTungguShapes = Shapes(
    // Card shape
    small = RoundedCornerShape(12.dp),   // Button
    medium = RoundedCornerShape(16.dp),  // Card
    large = RoundedCornerShape(20.dp),   // Dialog
    extraLarge = RoundedCornerShape(24.dp) // BottomSheet (top corners usage)
)

// Named shape references for clarity
val CardShape = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val ChipShape = RoundedCornerShape(20.dp)
val TextFieldShape = RoundedCornerShape(12.dp)
val BottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
val DialogShape = RoundedCornerShape(20.dp)
