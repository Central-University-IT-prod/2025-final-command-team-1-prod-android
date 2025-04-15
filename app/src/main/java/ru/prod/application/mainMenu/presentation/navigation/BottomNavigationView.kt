package ru.prod.application.mainMenu.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.prod.application.R
import ru.prod.application.core.domain.APP_DEFAULT_FONT_FAMILY
import androidx.navigation.NavDestination.Companion.hasRoute

// View со всеми табами
@Composable
fun BottomNavigationView(
    navController: NavController,
    tabs: List<TabModel>,
    navigateToCreateBookScreen: () -> Unit
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = colorResource(id = R.color.secondary_background),
        elevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        tabs.forEach { tab ->
            val isSelected = currentDestination?.hierarchy?.any {
                when {
                    it.hasRoute(tab.route::class) -> {
                        true
                    }
                    tab.route::class == HomeScreen::class -> {
                        it.hasRoute(BookDetailsScreen::class)
                    }
                    tab.route::class == ProfileScreen::class -> {
                        it.hasRoute(BookDetailsScreenInProfile::class)
                    }
                    tab.route::class == FavoritesScreen::class -> {
                        it.hasRoute(BookDetailsScreenInFavorites::class)
                    }
                    else -> false
                }
            } == true

            val tabContentColor = if (isSelected) {
                colorResource(id = R.color.darkest)
            } else {
                colorResource(id = R.color.text)
            }

            if (tab.title == "+") {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.plus_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 48.dp, height = 32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = colorResource(id = R.color.darkest))
                                .padding(4.dp),
                            tint = colorResource(id = R.color.white)
                        )
                    },
                    selected = isSelected,
                    onClick = navigateToCreateBookScreen,
                )
            } else {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            tab.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = tabContentColor
                        )
                    },
                    label = {
                        Text(
                            text = tab.title,
                            color = tabContentColor,
                            fontWeight = FontWeight.Thin,
                            fontSize = 10.sp,
                            fontFamily = APP_DEFAULT_FONT_FAMILY,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
