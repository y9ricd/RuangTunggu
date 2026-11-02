package com.ruangtunggu.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe route definitions for the RuangTunggu app.
 */
sealed interface Routes {

    // ── Auth Flow ──────────────────────────────────────────────
    @Serializable data object Splash : Routes
    @Serializable data object Onboarding : Routes
    @Serializable data object Login : Routes
    @Serializable data object Register : Routes
    @Serializable data object ForgotPassword : Routes
    @Serializable data class VerifyOtp(val email: String) : Routes
    @Serializable data object ResetPassword : Routes

    // ── Main / Home Flow ───────────────────────────────────────
    @Serializable data object Home : Routes
    @Serializable data class Search(val query: String = "") : Routes
    @Serializable data class PoliDetail(val hospitalId: String) : Routes
    @Serializable data class DoctorList(val poliId: String) : Routes
    @Serializable data class SelectSchedule(val doctorId: String) : Routes
    @Serializable data class ConfirmRegistration(val scheduleId: String) : Routes
    @Serializable data object RegistrationSuccess : Routes

    // ── Queue ──────────────────────────────────────────────────
    @Serializable data class QueueMonitor(val registrationId: String) : Routes

    // ── History ────────────────────────────────────────────────
    @Serializable data object History : Routes

    // ── Notifications ──────────────────────────────────────────
    @Serializable data object Notifications : Routes
    @Serializable data class NotificationDetail(val notificationId: String) : Routes

    // ── Profile ────────────────────────────────────────────────
    @Serializable data object Profile : Routes
    @Serializable data object EditProfile : Routes
    @Serializable data object About : Routes
}
