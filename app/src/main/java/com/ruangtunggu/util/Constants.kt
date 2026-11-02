package com.ruangtunggu.util

/**
 * Application-wide constants.
 */
object Constants {

    // ── Supabase Tables ────────────────────────────────────────
    const val TABLE_USERS = "users"
    const val TABLE_HOSPITALS = "hospitals"
    const val TABLE_POLYCLINICS = "polyclinics"
    const val TABLE_DOCTORS = "doctors"
    const val TABLE_SCHEDULES = "schedules"
    const val TABLE_REGISTRATIONS = "registrations"
    const val TABLE_QUEUES = "queues"
    const val TABLE_NOTIFICATIONS = "notifications"

    // ── Supabase Storage Buckets ───────────────────────────────
    const val BUCKET_AVATARS = "avatars"

    // ── SharedPreferences ──────────────────────────────────────
    const val PREFS_NAME = "ruangtunggu_prefs"
    const val PREF_ONBOARDING_SHOWN = "onboarding_shown"
    const val PREF_USER_ID = "user_id"
    const val PREF_ACCESS_TOKEN = "access_token"

    // ── Queue Defaults ─────────────────────────────────────────
    const val DEFAULT_ESTIMATED_MINUTES_PER_PATIENT = 10
    const val QUEUE_REFRESH_INTERVAL_MS = 5_000L

    // ── Pagination ─────────────────────────────────────────────
    const val DEFAULT_PAGE_SIZE = 20
}
