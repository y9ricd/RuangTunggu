package com.ruangtunggu.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ruangtunggu.ui.components.RTCard
import com.ruangtunggu.ui.components.RTChip
import com.ruangtunggu.ui.components.RTQueueCard
import com.ruangtunggu.ui.components.RTSearchBar
import com.ruangtunggu.ui.components.RTSkeletonCard
import com.ruangtunggu.ui.navigation.Routes
import com.ruangtunggu.ui.theme.Dimens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.PaddingScreen)
    ) {
        Spacer(modifier = Modifier.height(Dimens.SpacingLg))

        // ── 1. Greeting Header ──────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar with initials
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = uiState.patientName
                        .split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.toString() }
                        .joinToString("")
                        .ifEmpty { "NP" }

                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(Dimens.SpacingMd))

                Column {
                    Text(
                        text = "${uiState.greetingText} 👋",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = uiState.patientName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Notification Bell with Badge
            IconButton(onClick = { navController.navigate(Routes.Notifications) }) {
                BadgedBox(
                    badge = {
                        if (uiState.unreadNotifCount > 0) {
                            Badge {
                                Text(text = uiState.unreadNotifCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifikasi",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingLg))

        // ── 2. Search Bar ───────────────────────────────────────────────
        RTSearchBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearch = { query ->
                navController.navigate(Routes.Search(query = query))
            }
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingLg))

        // ── 3. Active Queue Card ────────────────────────────────────────
        if (uiState.isLoading) {
            RTSkeletonCard(height = 160.dp)
        } else if (uiState.activeQueue != null) {
            val queue = uiState.activeQueue!!
            RTQueueCard(
                queueNumber = queue.nomorAntrean,
                currentServing = queue.nomorDilayani,
                poliName = "Poli Anak",
                hospitalName = "RS Sehat Sidoarjo",
                date = "Kam, 22 Okt · 09:00",
                remainingCount = queue.sisaAntrean,
                estimatedMinutes = queue.estimasiMenit,
                onMonitorClick = {
                    navController.navigate(Routes.QueueMonitor(registrationId = queue.registrationId))
                }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXl))

        // ── 4. Quick Menu Grid (4 Layanan) ─────────────────────────────
        Text(
            text = "Layanan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMd))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickMenuItem(
                icon = Icons.Default.LocalHospital,
                title = "Cari RS",
                onClick = { navController.navigate(Routes.Search()) }
            )
            QuickMenuItem(
                icon = Icons.Default.CalendarMonth,
                title = "Jadwal",
                onClick = { navController.navigate(Routes.SelectSchedule(doctorId = "doc_01")) }
            )
            QuickMenuItem(
                icon = Icons.Default.History,
                title = "Riwayat",
                onClick = { navController.navigate(Routes.History) }
            )
            QuickMenuItem(
                icon = Icons.Default.Person,
                title = "Profil",
                onClick = { navController.navigate(Routes.Profile) }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXl))

        // ── 5. Poli Populer Chips ──────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Poli populer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = { navController.navigate(Routes.Search()) }) {
                Text(
                    text = "Lihat semua",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXs))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSm),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.popularPolis) { poli ->
                RTChip(
                    text = poli.nama,
                    selected = uiState.selectedPoliId == poli.id,
                    onClick = { viewModel.onPoliSelected(poli.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXl))

        // ── 6. Rumah Sakit Terdekat ────────────────────────────────────
        Text(
            text = "Rumah sakit terdekat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMd))

        uiState.nearbyHospitals.forEach { hospital ->
            RTCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.SpacingMd)
                    .clickable {
                        navController.navigate(Routes.PoliDetail(hospitalId = hospital.id))
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpacingLg),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hospital.nama,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingXs))
                        Text(
                            text = hospital.alamat,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.SpacingSm))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(horizontal = Dimens.SpacingMd, vertical = Dimens.SpacingXs)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${hospital.rating} · 1,2 km",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingXxl))
    }
}

@Composable
private fun QuickMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingSm))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

