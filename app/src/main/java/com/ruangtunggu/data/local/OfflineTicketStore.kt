package com.ruangtunggu.data.local

import android.content.Context
import android.content.SharedPreferences
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.QueueHistoryItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class StoredUserAccount(
    val profile: PatientProfile,
    val passwordPlain: String
)

/**
 * Persistent Local Database & Offline-First Ticket Store:
 * - Stores registered user accounts (NIK, Email, Password, Full Name, Birth Date)
 * - Stores offline QR Ticket payload for zero-signal hospital areas
 * - Stores real user Queue History (starts 100% empty on fresh accounts!)
 */
@Singleton
class OfflineTicketStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ruangtunggu_local_db_v3", Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun saveAccount(account: StoredUserAccount) {
        val currentList = getAllAccounts().toMutableList()
        currentList.removeAll {
            it.profile.nik.equals(account.profile.nik, ignoreCase = true) ||
                it.profile.email.equals(account.profile.email, ignoreCase = true)
        }
        currentList.add(0, account)
        prefs.edit()
            .putString(KEY_ACCOUNTS, json.encodeToString(currentList))
            .putString(KEY_CURRENT_PROFILE, json.encodeToString(account.profile))
            .apply()
    }

    fun getAllAccounts(): List<StoredUserAccount> {
        val raw = prefs.getString(KEY_ACCOUNTS, null) ?: return emptyList()
        return runCatching { json.decodeFromString<List<StoredUserAccount>>(raw) }.getOrDefault(emptyList())
    }

    fun findAccountByIdentifier(nikOrEmail: String): StoredUserAccount? {
        val clean = nikOrEmail.trim()
        return getAllAccounts().firstOrNull {
            it.profile.email.equals(clean, ignoreCase = true) ||
                it.profile.nik.equals(clean, ignoreCase = true) ||
                it.profile.phone.equals(clean, ignoreCase = true)
        }
    }

    fun updateAccountPassword(nikOrEmail: String, newPassword: String): Boolean {
        val clean = nikOrEmail.trim()
        val accounts = getAllAccounts().toMutableList()
        val index = accounts.indexOfFirst {
            it.profile.email.equals(clean, ignoreCase = true) ||
                it.profile.nik.equals(clean, ignoreCase = true)
        }
        if (index >= 0) {
            accounts[index] = accounts[index].copy(passwordPlain = newPassword)
            prefs.edit().putString(KEY_ACCOUNTS, json.encodeToString(accounts)).apply()
            return true
        }
        return false
    }

    fun saveCurrentProfile(profile: PatientProfile) {
        prefs.edit().putString(KEY_CURRENT_PROFILE, json.encodeToString(profile)).apply()
        val accounts = getAllAccounts().toMutableList()
        val idx = accounts.indexOfFirst { it.profile.id == profile.id || it.profile.nik == profile.nik }
        if (idx >= 0) {
            accounts[idx] = accounts[idx].copy(profile = profile)
            prefs.edit().putString(KEY_ACCOUNTS, json.encodeToString(accounts)).apply()
        }
    }

    fun getCurrentProfile(): PatientProfile? {
        val raw = prefs.getString(KEY_CURRENT_PROFILE, null) ?: return null
        return runCatching { json.decodeFromString<PatientProfile>(raw) }.getOrNull()
    }

    fun saveOfflineTicket(queue: ActiveQueue?) {
        if (queue == null) {
            prefs.edit().remove(KEY_ACTIVE_TICKET).apply()
        } else {
            val serialized = json.encodeToString(queue.copy(isOfflineCached = true))
            prefs.edit().putString(KEY_ACTIVE_TICKET, serialized).apply()
        }
    }

    fun getOfflineTicket(): ActiveQueue? {
        val raw = prefs.getString(KEY_ACTIVE_TICKET, null) ?: return null
        return runCatching { json.decodeFromString<ActiveQueue>(raw) }.getOrNull()
    }

    fun clearOfflineTicket() {
        prefs.edit().remove(KEY_ACTIVE_TICKET).apply()
    }

    fun saveHistoryList(list: List<QueueHistoryItem>) {
        prefs.edit().putString(KEY_HISTORY_LIST, json.encodeToString(list)).apply()
    }

    fun getHistoryList(): List<QueueHistoryItem> {
        val raw = prefs.getString(KEY_HISTORY_LIST, null) ?: return emptyList()
        return runCatching { json.decodeFromString<List<QueueHistoryItem>>(raw) }.getOrDefault(emptyList())
    }

    fun clearHistoryList() {
        prefs.edit().remove(KEY_HISTORY_LIST).apply()
    }

    companion object {
        private const val KEY_ACCOUNTS = "key_registered_accounts_json"
        private const val KEY_CURRENT_PROFILE = "key_current_patient_profile_json"
        private const val KEY_ACTIVE_TICKET = "key_offline_active_ticket_json"
        private const val KEY_HISTORY_LIST = "key_user_queue_history_json"
    }
}
