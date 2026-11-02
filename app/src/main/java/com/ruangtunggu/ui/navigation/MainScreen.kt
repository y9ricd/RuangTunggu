package com.ruangtunggu.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ruangtunggu.ui.components.BottomNavItem
import com.ruangtunggu.ui.components.RTBottomNavBar

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

    // Determine current route string for the bottom bar
    val currentRoute = remember(currentDestination) {
        when {
            currentDestination?.hasRoute(Routes.Home::class) == true -> BottomNavItem.Beranda.route
            currentDestination?.hasRoute(Routes.History::class) == true -> BottomNavItem.Riwayat.route
            currentDestination?.hasRoute(Routes.Profile::class) == true -> BottomNavItem.Profil.route
            else -> BottomNavItem.Beranda.route
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                RTBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        val destination = when (route) {
                            BottomNavItem.Beranda.route -> Routes.Home
                            BottomNavItem.Antrean.route -> Routes.QueueMonitor(registrationId = "")
                            BottomNavItem.Riwayat.route -> Routes.History
                            BottomNavItem.Profil.route -> Routes.Profile
                            else -> Routes.Home
                        }
                        mainNavController.navigate(destination) {
                            popUpTo(mainNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
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
