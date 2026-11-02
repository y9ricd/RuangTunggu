package com.ruangtunggu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute

import com.ruangtunggu.ui.screens.auth.ForgotPasswordScreen
import com.ruangtunggu.ui.screens.auth.LoginScreen
import com.ruangtunggu.ui.screens.auth.RegisterScreen
import com.ruangtunggu.ui.screens.home.HomeScreen
import com.ruangtunggu.ui.screens.onboarding.OnboardingScreen
import com.ruangtunggu.ui.screens.queue.QueueMonitorScreen
import com.ruangtunggu.ui.screens.splash.SplashScreen

/**
 * Root navigation graph for the RuangTunggu app.
 *
 * @param navController  The [NavHostController] driving navigation.
 * @param startDestination The route to start at (Splash, Home, etc.).
 * @param modifier       Optional [Modifier] applied to the NavHost.
 */
@Composable
fun RuangTungguNavGraph(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // ── Auth Flow ──────────────────────────────────────────
        composable<Routes.Splash> {
            SplashScreen(navController = navController)
        }

        composable<Routes.Onboarding> {
            OnboardingScreen(navController = navController)
        }

        composable<Routes.Login> {
            LoginScreen(navController = navController)
        }

        composable<Routes.Register> {
            RegisterScreen(navController = navController)
        }

        composable<Routes.ForgotPassword> {
            ForgotPasswordScreen(navController = navController)
        }

        composable<Routes.VerifyOtp> { backStackEntry ->
            ForgotPasswordScreen(navController = navController)
        }

        composable<Routes.ResetPassword> {
            ForgotPasswordScreen(navController = navController)
        }

        // ── Home / Registration Flow ───────────────────────────
        composable<Routes.Home> {
            HomeScreen(navController = navController)
        }

        composable<Routes.Search> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Search>()
            // TODO: SearchScreen(navController, query = route.query)
        }

        composable<Routes.PoliDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.PoliDetail>()
            // TODO: PoliDetailScreen(navController, hospitalId = route.hospitalId)
        }

        composable<Routes.DoctorList> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.DoctorList>()
            // TODO: DoctorListScreen(navController, poliId = route.poliId)
        }

        composable<Routes.SelectSchedule> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.SelectSchedule>()
            // TODO: SelectScheduleScreen(navController, doctorId = route.doctorId)
        }

        composable<Routes.ConfirmRegistration> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ConfirmRegistration>()
            // TODO: ConfirmRegistrationScreen(navController, scheduleId = route.scheduleId)
        }

        composable<Routes.RegistrationSuccess> {
            // TODO: RegistrationSuccessScreen(navController)
        }

        // ── Queue ──────────────────────────────────────────────
        composable<Routes.QueueMonitor> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.QueueMonitor>()
            QueueMonitorScreen(navController = navController, registrationId = route.registrationId)
        }

        // ── History ────────────────────────────────────────────
        composable<Routes.History> {
            // TODO: HistoryScreen(navController)
        }

        // ── Notifications ──────────────────────────────────────
        composable<Routes.Notifications> {
            // TODO: NotificationsScreen(navController)
        }

        composable<Routes.NotificationDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.NotificationDetail>()
            // TODO: NotificationDetailScreen(navController, notificationId = route.notificationId)
        }

        // ── Profile ────────────────────────────────────────────
        composable<Routes.Profile> {
            // TODO: ProfileScreen(navController)
        }

        composable<Routes.EditProfile> {
            // TODO: EditProfileScreen(navController)
        }

        composable<Routes.About> {
            // TODO: AboutScreen(navController)
        }
    }
}
