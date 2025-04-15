package ru.prod.application.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.prod.application.R

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Black
    ),

    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),

    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),

    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
)