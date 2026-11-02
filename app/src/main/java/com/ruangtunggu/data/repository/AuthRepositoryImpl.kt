package com.ruangtunggu.data.repository

import com.ruangtunggu.data.local.PreferenceManager
import com.ruangtunggu.data.model.User
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    // In-memory registered user cache (pre-seeded with demo account)
    private val users = mutableListOf(
        User(
            id = "user_demo_01",
            nik = "1234567890123456",
            email = "pasien@ruangtunggu.com",
            nama = "Nama Pasien",
            noHp = "081234567890",
            createdAt = "2026-10-01"
        )
    )

    private val passwords = mutableMapOf(
        "pasien@ruangtunggu.com" to "password123",
        "1234567890123456" to "password123"
    )

    override suspend fun login(
        identifier: String,
        password: String,
        captchaToken: String
    ): Result<User> {
        delay(600) // Simulate network latency

        if (captchaToken.isBlank()) {
            return Result.failure(Exception("Verifikasi CAPTCHA wajib diselesaikan"))
        }

        val trimmed = identifier.trim()
        val user = users.find { it.email.equals(trimmed, ignoreCase = true) || it.nik == trimmed }

        if (user == null) {
            return Result.failure(Exception("NIK atau Email tidak terdaftar"))
        }

        val expectedPassword = passwords[user.email] ?: passwords[user.nik]
        if (expectedPassword != password) {
            return Result.failure(Exception("Kata sandi yang Anda masukkan salah"))
        }

        // Save session
        preferenceManager.savedUserId = user.id
        preferenceManager.savedUserName = user.nama
        preferenceManager.savedUserEmail = user.email

        return Result.success(user)
    }

    override suspend fun register(
        nama: String,
        nik: String,
        email: String,
        noHp: String,
        password: String,
        captchaToken: String
    ): Result<User> {
        delay(700)

        if (captchaToken.isBlank()) {
            return Result.failure(Exception("Verifikasi CAPTCHA wajib diselesaikan"))
        }

        if (users.any { it.email.equals(email.trim(), ignoreCase = true) }) {
            return Result.failure(Exception("Email sudah terdaftar. Silakan login"))
        }

        if (users.any { it.nik == nik.trim() }) {
            return Result.failure(Exception("NIK sudah terdaftar dalam sistem"))
        }

        val newUser = User(
            id = UUID.randomUUID().toString(),
            nik = nik.trim(),
            email = email.trim(),
            nama = nama.trim(),
            noHp = noHp.trim(),
            createdAt = "2026-11-02"
        )

        users.add(newUser)
        passwords[newUser.email] = password
        passwords[newUser.nik] = password

        // Auto login on register
        preferenceManager.savedUserId = newUser.id
        preferenceManager.savedUserName = newUser.nama
        preferenceManager.savedUserEmail = newUser.email

        return Result.success(newUser)
    }

    override suspend fun logout(): Result<Unit> {
        preferenceManager.clearSession()
        return Result.success(Unit)
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        delay(500)
        val user = users.find { it.email.equals(email.trim(), ignoreCase = true) }
            ?: return Result.failure(Exception("Email tidak ditemukan"))
        return Result.success(Unit)
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Unit> {
        delay(400)
        // Demo allows "1234" or any 4 digit code
        return if (otp.trim() == "1234" || otp.trim().length == 4) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Kode verifikasi OTP salah"))
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        delay(500)
        val email = preferenceManager.savedUserEmail ?: "pasien@ruangtunggu.com"
        passwords[email] = newPassword
        return Result.success(Unit)
    }

    override suspend fun getCurrentUser(): Result<User?> {
        val currentId = preferenceManager.savedUserId ?: return Result.success(null)
        val user = users.find { it.id == currentId }
        return Result.success(user)
    }

    override suspend fun isLoggedIn(): Boolean {
        return !preferenceManager.savedUserId.isNullOrBlank()
    }
}

