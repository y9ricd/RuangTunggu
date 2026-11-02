package com.ruangtunggu.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Describes each item in the bottom navigation bar.
 */
data class BottomNavItem(
    val label: String,
    val route: Routes,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * The list of top-level bottom navigation destinations.
 */
val bottomNavItems = listOf(
    BottomNavItem(
        label = "Beranda",
        route = Routes.Home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        label = "Antrean",
        route = Routes.QueueMonitor(registrationId = ""),
        selectedIcon = Icons.Filled.QueuePlayNext,
        unselectedIcon = Icons.Outlined.QueuePlayNext
    ),
    BottomNavItem(
        label = "Riwayat",
        route = Routes.History,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    ),
    BottomNavItem(
        label = "Profil",
        route = Routes.Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

/**
 * Routes that should show the bottom navigation bar.
 */
private val bottomNavRoutes = listOf(
    Routes.Home::class,
    Routes.History::class,
    Routes.Profile::class
)

/**
 * Main scaffold with bottom navigation for the RuangTunggu app.
 */
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = remember(currentDestination) {
        bottomNavRoutes.any { routeClass ->
            currentDestination?.hasRoute(routeClass) == true
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                RTBottomNavBar(
                    navController = mainNavController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        RuangTungguNavGraph(
            navController = mainNavController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Bottom navigation bar composable.
 */
@Composable
private fun RTBottomNavBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hasRoute(item.route::class) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building up a back stack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) }
            )
        }
    }
}
