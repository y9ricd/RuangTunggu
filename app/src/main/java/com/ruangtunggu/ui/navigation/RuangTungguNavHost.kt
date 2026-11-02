package com.ruangtunggu.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ruangtunggu.ui.screens.beranda.BerandaScreen
import com.ruangtunggu.ui.screens.booking.CariRsPoliScreen
import com.ruangtunggu.ui.screens.booking.KonfirmasiPendaftaranScreen
import com.ruangtunggu.ui.screens.booking.PendaftaranBerhasilScreen
import com.ruangtunggu.ui.screens.booking.PilihDokterJadwalScreen
import com.ruangtunggu.ui.screens.booking.PilihPoliScreen
import com.ruangtunggu.ui.screens.login.LoginScreen
import com.ruangtunggu.ui.screens.register.RegisterScreen
import com.ruangtunggu.ui.screens.splash.SplashScreen
import com.ruangtunggu.ui.screens.support.AturPengingatScreen
import com.ruangtunggu.ui.screens.support.BeriPenilaianScreen
import com.ruangtunggu.ui.screens.support.ChatPetugasScreen
import com.ruangtunggu.ui.screens.support.ForgotPasswordScreen
import com.ruangtunggu.ui.screens.support.NotifikasiScreen
import com.ruangtunggu.ui.screens.support.RescheduleJadwalScreen
import com.ruangtunggu.ui.screens.support.UbahProfilScreen
import com.ruangtunggu.ui.screens.ticket.DatangKeLoketScreen
import com.ruangtunggu.ui.screens.ticket.TiketQrScreen

@Suppress("FunctionName")
@Composable
fun RuangTungguNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = {
            fadeIn(tween(360, easing = FastOutSlowInEasing)) +
                slideInHorizontally(
                    initialOffsetX = { it / 6 },
                    animationSpec = tween(360, easing = FastOutSlowInEasing)
                )
        },
        exitTransition = {
            fadeOut(tween(300, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            fadeIn(tween(340, easing = FastOutSlowInEasing)) +
                slideInHorizontally(
                    initialOffsetX = { -it / 6 },
                    animationSpec = tween(340, easing = FastOutSlowInEasing)
                )
        },
        popExitTransition = {
            fadeOut(tween(280, easing = FastOutSlowInEasing)) +
                slideOutHorizontally(
                    targetOffsetX = { it / 6 },
                    animationSpec = tween(280, easing = FastOutSlowInEasing)
                )
        }
    ) {
        composable<SplashRoute> {
            SplashScreen(
                onMulaiClick = { navController.navigate(RegisterRoute) },
                onMasukClick = { navController.navigate(LoginRoute) }
            )
        }

        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(BerandaRoute()) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(RegisterRoute) },
                onForgotPasswordClick = { navController.navigate(ForgotPasswordRoute) }
            )
        }

        composable<ForgotPasswordRoute> {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(BerandaRoute()) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo(SplashRoute) { inclusive = false }
                    }
                }
            )
        }

        composable<BerandaRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<BerandaRoute>()
            BerandaScreen(
                initialTab = route.initialTab,
                onNavigateToCariRs = { filter ->
                    navController.navigate(CariRsPoliRoute(initialPoliFilter = filter))
                },
                onNavigateToPilihPoli = { hospital ->
                    navController.navigate(
                        PilihPoliRoute(
                            hospitalId = hospital.id,
                            hospitalName = hospital.name
                        )
                    )
                },
                onNavigateToTiketQr = { navController.navigate(TiketQrRoute) },
                onNavigateToDatangKeLoket = { navController.navigate(DatangKeLoketRoute) },
                onNavigateToChatPetugas = { navController.navigate(ChatPetugasRoute) },
                onNavigateToNotifikasi = { navController.navigate(NotifikasiRoute) },
                onNavigateToAturPengingat = { navController.navigate(AturPengingatRoute) },
                onNavigateToBeriPenilaian = { navController.navigate(BeriPenilaianRoute) },
                onNavigateToReschedule = { navController.navigate(RescheduleJadwalRoute) },
                onNavigateToUbahProfil = { navController.navigate(UbahProfilRoute) },
                onLogoutClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<CariRsPoliRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CariRsPoliRoute>()
            CariRsPoliScreen(
                initialFilter = route.initialPoliFilter,
                onBackClick = { navController.popBackStack() },
                onContinueToPilihPoli = { hospital ->
                    navController.navigate(
                        PilihPoliRoute(
                            hospitalId = hospital.id,
                            hospitalName = hospital.name
                        )
                    )
                }
            )
        }

        composable<PilihPoliRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PilihPoliRoute>()
            PilihPoliScreen(
                hospitalId = route.hospitalId,
                hospitalName = route.hospitalName,
                onBackClick = { navController.popBackStack() },
                onContinueToDokter = { poli ->
                    navController.navigate(
                        PilihDokterJadwalRoute(
                            hospitalId = route.hospitalId,
                            hospitalName = route.hospitalName,
                            poliId = poli.id,
                            poliName = poli.name,
                            poliShortName = poli.shortName
                        )
                    )
                }
            )
        }

        composable<PilihDokterJadwalRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PilihDokterJadwalRoute>()
            PilihDokterJadwalScreen(
                hospitalName = route.hospitalName,
                poliName = route.poliName,
                poliShortName = route.poliShortName,
                onBackClick = { navController.popBackStack() },
                onContinueToConfirm = { doctor, dateLabel, slotTime ->
                    navController.navigate(
                        KonfirmasiPendaftaranRoute(
                            hospitalId = route.hospitalId,
                            hospitalName = route.hospitalName,
                            poliId = route.poliId,
                            poliName = route.poliName,
                            poliShortName = route.poliShortName,
                            doctorId = doctor.id,
                            doctorName = doctor.name,
                            dateLabel = dateLabel,
                            slotTime = slotTime
                        )
                    )
                }
            )
        }

        composable<KonfirmasiPendaftaranRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<KonfirmasiPendaftaranRoute>()
            KonfirmasiPendaftaranScreen(
                hospitalId = route.hospitalId,
                hospitalName = route.hospitalName,
                poliId = route.poliId,
                poliName = route.poliName,
                poliShortName = route.poliShortName,
                doctorId = route.doctorId,
                doctorName = route.doctorName,
                dateLabel = route.dateLabel,
                slotTime = route.slotTime,
                onBackClick = { navController.popBackStack() },
                onBookingConfirmed = {
                    navController.navigate(PendaftaranBerhasilRoute) {
                        popUpTo(BerandaRoute()) { inclusive = false }
                    }
                }
            )
        }

        composable<PendaftaranBerhasilRoute> {
            PendaftaranBerhasilScreen(
                onPantauAntreanClick = {
                    navController.navigate(BerandaRoute(initialTab = "ANTREAN")) {
                        popUpTo(BerandaRoute()) { inclusive = true }
                    }
                },
                onBackToBerandaClick = {
                    navController.navigate(BerandaRoute(initialTab = "BERANDA")) {
                        popUpTo(BerandaRoute()) { inclusive = true }
                    }
                }
            )
        }

        composable<TiketQrRoute> {
            TiketQrScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<DatangKeLoketRoute> {
            DatangKeLoketScreen(
                onBackClick = { navController.popBackStack() },
                onShowQrClick = { navController.navigate(TiketQrRoute) }
            )
        }

        composable<ChatPetugasRoute> {
            ChatPetugasScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<NotifikasiRoute> {
            NotifikasiScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AturPengingatRoute> {
            AturPengingatScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<BeriPenilaianRoute> {
            BeriPenilaianScreen(
                onBackClick = { navController.popBackStack() },
                onReviewSubmitted = {
                    navController.navigate(BerandaRoute(initialTab = "BERANDA")) {
                        popUpTo(BerandaRoute()) { inclusive = true }
                    }
                }
            )
        }

        composable<RescheduleJadwalRoute> {
            RescheduleJadwalScreen(
                onBackClick = { navController.popBackStack() },
                onRescheduleConfirmed = {
                    navController.navigate(BerandaRoute(initialTab = "ANTREAN")) {
                        popUpTo(BerandaRoute()) { inclusive = true }
                    }
                }
            )
        }

        composable<UbahProfilRoute> {
            UbahProfilScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
