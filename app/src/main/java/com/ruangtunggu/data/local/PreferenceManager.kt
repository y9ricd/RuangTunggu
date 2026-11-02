package com.ruangtunggu.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ruang_tunggu_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_USER_NAME = "key_user_name"
        private const val KEY_USER_EMAIL = "key_user_email"
        private const val KEY_BIOMETRIC_ENABLED = "key_biometric_enabled"
    }

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()

    var savedUserId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var savedUserName: String?
        get() = prefs.getString(KEY_USER_NAME, "Pasien")
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var savedUserEmail: String?
        get() = prefs.getString(KEY_USER_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var isBiometricEnabled: Boolean
        get() = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()

    fun clearSession() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_EMAIL)
            .apply()
    }
}

