package com.ruangtunggu.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueuePlayNext
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.ruangtunggu.ui.theme.RuangTungguTheme

/**
 * Bottom navigation destinations.
 */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Beranda : BottomNavItem(
        route = "beranda",
        label = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Antrean : BottomNavItem(
        route = "antrean",
        label = "Antrean",
        selectedIcon = Icons.Filled.QueuePlayNext,
        unselectedIcon = Icons.Outlined.QueuePlayNext
    )

    data object Riwayat : BottomNavItem(
        route = "riwayat",
        label = "Riwayat",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )

    data object Profil : BottomNavItem(
        route = "profil",
        label = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    companion object {
        val items = listOf(Beranda, Antrean, Riwayat, Profil)
    }
}

/**
 * Bottom navigation bar with 4 tabs.
 *
 * @param currentRoute     The currently selected route.
 * @param onNavigate       Callback when a tab is tapped (returns route string).
 * @param antreanBadgeCount Number to show as badge on the Antrean tab; 0 = hidden.
 * @param modifier         Outer modifier.
 */
@Composable
fun RTBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    antreanBadgeCount: Int = 0
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        BottomNavItem.items.forEach { item ->
            val selected = currentRoute == item.route
            val icon = if (selected) item.selectedIcon else item.unselectedIcon

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    if (item is BottomNavItem.Antrean && antreanBadgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = antreanBadgeCount.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = item.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────

@Preview(name = "BottomNav – Light", showBackground = true)
@Composable
private fun PreviewBottomNavLight() {
    RuangTungguTheme(darkTheme = false) {
        RTBottomNavBar(
            currentRoute = "beranda",
            onNavigate = {},
            antreanBadgeCount = 3
        )
    }
}

@Preview(name = "BottomNav – Dark", showBackground = true)
@Composable
private fun PreviewBottomNavDark() {
    RuangTungguTheme(darkTheme = true) {
        RTBottomNavBar(
            currentRoute = "antrean",
            onNavigate = {},
            antreanBadgeCount = 1
        )
    }
}
