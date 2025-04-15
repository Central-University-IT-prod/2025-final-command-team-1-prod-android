package ru.prod.application.mainMenu.presentation.screens.home

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class BannerModel(
    val title: String,
    val subtitle: String,
    val brush: Brush,
    val image: Painter,
    val link: String
)
