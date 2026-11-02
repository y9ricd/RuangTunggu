package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.User

interface AuthRepository {
    suspend fun login(identifier: String, password: String, captchaToken: String): Result<User>
    suspend fun register(
        nama: String,
        nik: String,
        email: String,
        noHp: String,
        password: String,
        captchaToken: String
    ): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, otp: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun isLoggedIn(): Boolean
}
