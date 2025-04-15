package ru.prod.application.mainMenu.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.prod.application.R
import ru.prod.application.mainMenu.presentation.screens.applicationsScreen.ApplicationsScreenView
import ru.prod.application.mainMenu.presentation.screens.details.BookDetailsScreen
import ru.prod.application.mainMenu.presentation.screens.favorites.FavoritesScreen
import ru.prod.application.mainMenu.presentation.screens.favorites.FavoritesViewModel
import ru.prod.application.mainMenu.presentation.screens.home.HomeScreenCompose
import ru.prod.application.mainMenu.presentation.screens.home.HomeViewModel
import ru.prod.application.mainMenu.presentation.screens.profileScreen.ProfileScreenView
import ru.prod.application.mainMenu.presentation.screens.profileScreen.ProfileScreenViewModel
import ru.prod.application.mainMenu.presentation.screens.reviewsScreen.ReviewsScreenView

// Модель для хранения данных о странице в tab navigation
data class TabModel(val route: Screen, val title: String, val icon: Painter)

sealed interface Screen

// Экраны
@Serializable
data object HomeScreen : Screen

@Serializable
data object ApplicationsScreen : Screen

@Serializable
data object AddBookScreen : Screen

@Serializable
data object ProfileScreen : Screen

@Serializable
data object FavoritesScreen : Screen

@Serializable
data class BookDetailsScreen(val id: Int) : Screen

@Serializable
data class BookDetailsScreenInProfile(val id: Int) : Screen

@Serializable
data class BookDetailsScreenInFavorites(val id: Int) : Screen

@Serializable
data class ReviewsScreen(val username: String) : Screen

@Composable
fun TabNavigator(
    start: String,
    navigateToCreateBookScreen: () -> Unit,
    navigateToAIChat: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToBrowsePhoto: (imageUrl: String) -> Unit,
) {
    val navController = rememberNavController()

    val tabs = listOf(
        TabModel(
            HomeScreen,
            "Книги",
            painterResource(id = R.drawable.home),
        ),
        TabModel(
            ApplicationsScreen,
            "Брони",
            painterResource(id = R.drawable.shopping_cart),
        ),
        TabModel(
            AddBookScreen,
            "+",
            painterResource(id = R.drawable.plus_icon),
        ),
        TabModel(
            FavoritesScreen,
            "Моё",
            painterResource(id = R.drawable.heart),
        ),
        TabModel(
            ProfileScreen,
            "Профиль",
            painterResource(id = R.drawable.user),
        ),
    )

    Scaffold(bottomBar = {
        BottomNavigationView(
            navController,
            tabs,
            navigateToCreateBookScreen = navigateToCreateBookScreen,
        )
    }, containerColor = colorResource(id = R.color.background)) { innerPadding ->
        val homeViewModel = hiltViewModel<HomeViewModel>()
        val profileViewModel = hiltViewModel<ProfileScreenViewModel>()
        val favoritesViewModel = hiltViewModel<FavoritesViewModel>()

        NavHost(
            navController = navController,
            startDestination = when (start) {
                "profile" -> ProfileScreen
                "applications" -> ApplicationsScreen
                else -> HomeScreen
            },
            modifier = Modifier
                .background(colorResource(id = R.color.secondary_background))
                .padding(innerPadding),
        ) {
            composable<HomeScreen>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                HomeScreenCompose(onBookClick = {
                    navController.navigate(BookDetailsScreen(it.id))
                }, navigateToSearch = navigateToSearch, viewModel = homeViewModel)
            }
            composable<ApplicationsScreen>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                ApplicationsScreenView()
            }
            composable<FavoritesScreen>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                FavoritesScreen(onBookClicked = {
                    navController.navigate(BookDetailsScreenInFavorites(it))
                }, viewModel = favoritesViewModel)
            }
            composable<BookDetailsScreenInFavorites>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                val route = it.toRoute<BookDetailsScreenInFavorites>()
                BookDetailsScreen(bookId = route.id, onBackClick = {
                    navController.popBackStack()
                }, navigateToBrowsePhoto = navigateToBrowsePhoto,
                    navigateToApplications = { navController.navigate(ApplicationsScreen) }, isMyBook = false)
            }
            composable<BookDetailsScreen>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                val route = it.toRoute<BookDetailsScreen>()
                BookDetailsScreen(bookId = route.id, onBackClick = {
                    navController.popBackStack()
                }, navigateToBrowsePhoto = navigateToBrowsePhoto,
                    navigateToApplications = { navController.navigate(ApplicationsScreen) }, isMyBook = false)
            }

            composable<ProfileScreen>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None })
            {
                ProfileScreenView(
                    navigateToAIChat = navigateToAIChat,
                    onBookClick = { navController.navigate(BookDetailsScreenInProfile(it)) },
                    navigateToReviews = { navController.navigate(ReviewsScreen(it)) },
                    viewModel = profileViewModel
                )
            }

            composable<ReviewsScreen>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(400)
                    )
                }
            ) {
                val route = it.toRoute<ReviewsScreen>()
                ReviewsScreenView(
                    popBackStack = { navController.popBackStack() },
                    username = route.username
                )
            }

            composable<BookDetailsScreenInProfile>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }) {
                val route = it.toRoute<BookDetailsScreenInProfile>()
                BookDetailsScreen(bookId = route.id, onBackClick = {
                    navController.popBackStack()
                }, navigateToBrowsePhoto = navigateToBrowsePhoto,
                    navigateToApplications = { navController.navigate(ApplicationsScreen) }, isMyBook = true)
            }
        }
    }
}
