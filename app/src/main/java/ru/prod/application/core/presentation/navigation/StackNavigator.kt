package ru.prod.application.core.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.prod.application.R
import ru.prod.application.ai.presentation.screens.AIChatScreenView
import ru.prod.application.auth.presentation.screens.loginScreen.LoginScreenView
import ru.prod.application.auth.presentation.screens.signUpScreen.SignUpScreenView
import ru.prod.application.mainMenu.presentation.navigation.BookDetailsScreen
import ru.prod.application.mainMenu.presentation.navigation.TabNavigator
import ru.prod.application.mainMenu.presentation.screens.addBookScreen.AddBookScreenView
import ru.prod.application.mainMenu.presentation.screens.browse_photo.BrowsePhotoScreenRoot
import ru.prod.application.mainMenu.presentation.screens.search.SearchScreenView

// Основной граф навигации
@Composable
fun StackNavigator(authorized: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        startDestination = if (authorized) Destinations.MainMenu("home") else Destinations.SignUpScreen
    ) {
        composable<Destinations.LoginScreen> {
            LoginScreenView(
                navigateToSignUpScreen = {
                    navController.navigate(Destinations.SignUpScreen) {
                        popUpTo<Destinations.LoginScreen> {
                            inclusive = true
                        }
                    }
                },
                navigateToMainFlow = {
                    navController.navigate(Destinations.MainMenu("home")) {
                        popUpTo<Destinations.LoginScreen> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Destinations.SignUpScreen> {
            SignUpScreenView(
                navigateToLoginScreen = {
                    navController.navigate(Destinations.LoginScreen) {
                        popUpTo<Destinations.SignUpScreen> {
                            inclusive = true
                        }
                    }
                },
                navigateToMainFlow = {
                    navController.navigate(Destinations.MainMenu("home")) {
                        popUpTo<Destinations.SignUpScreen> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Destinations.AIChat> {
            AIChatScreenView()
        }

        composable<Destinations.MainMenu> {
            val route = it.toRoute<Destinations.MainMenu>()
            TabNavigator(
                start = route.start,
                navigateToCreateBookScreen = { navController.navigate(Destinations.AddBookScreenView) },
                navigateToAIChat = { navController.navigate(Destinations.AIChat) },
                navigateToSearch = { navController.navigate(Destinations.SearchScreen) },
                navigateToBrowsePhoto = { navController.navigate(Destinations.BrowsePhotoScreen(it)) },
            )
        }

        composable<Destinations.SearchScreen> {
            SearchScreenView({ navController.navigate(Destinations.BookDetailsScreenInSearch(it)) })
        }

        composable<Destinations.BrowsePhotoScreen> {
            val route = it.toRoute<Destinations.BrowsePhotoScreen>()
            BrowsePhotoScreenRoot(route.imageUrl)
        }

        composable<Destinations.AddBookScreenView> {
            AddBookScreenView(
                popBackStack = navController::popBackStack,
                navigateToMainMenu = { navController.navigate(Destinations.MainMenu("profile")) }
            )
        }

        composable<Destinations.BookDetailsScreenInSearch>() {
            val route = it.toRoute<BookDetailsScreen>()
            ru.prod.application.mainMenu.presentation.screens.details.BookDetailsScreen(
                bookId = route.id,
                onBackClick = {
                    navController.popBackStack()
                },
                inSearch = true,
                navigateToBrowsePhoto = { imageUrl ->
                    navController.navigate(
                        Destinations.BrowsePhotoScreen(imageUrl)
                    )
                },
                navigateToApplications = {
                    navController.navigate(Destinations.MainMenu("applications"))
                },
                isMyBook = false,
            )
        }
    }
}