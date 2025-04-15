package ru.prod.application.core.presentation.navigation

import kotlinx.serialization.Serializable

// Экраны приложния(вершины графа)
object Destinations {
    @Serializable
    data object SignUpScreen

    @Serializable
    data object LoginScreen

    @Serializable
    data class MainMenu(val start: String)

    @Serializable
    data object SearchScreen

    @Serializable
    data object AIChat

    @Serializable
    data object AddBookScreenView

    @Serializable
    data class BrowsePhotoScreen(val imageUrl: String)

    @Serializable
    data class BookDetailsScreenInSearch(val id: Int)
}