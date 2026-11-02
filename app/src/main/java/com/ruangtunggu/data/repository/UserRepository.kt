package com.ruangtunggu.data.repository

import com.ruangtunggu.data.model.User

interface UserRepository {
    suspend fun getProfile(userId: String): Result<User>
    suspend fun updateProfile(userId: String, nama: String, noHp: String): Result<User>
    suspend fun uploadAvatar(userId: String, imageBytes: ByteArray): Result<String>
}
