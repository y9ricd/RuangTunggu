package com.ruangtunggu.domain.usecase

import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nikOrEmail: String,
        password: String,
        captchaChecked: Boolean
    ): Result<PatientProfile> {
        if (nikOrEmail.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email/password salah"))
        }
        if (!captchaChecked) {
            return Result.failure(IllegalArgumentException("Harap centang verifikasi CAPTCHA"))
        }
        return authRepository.login(nikOrEmail.trim(), password)
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val queueRepository: QueueRepository
) {
    suspend operator fun invoke(
        fullName: String,
        emailOrPhone: String,
        nik: String,
        birthPlaceDate: String,
        password: String,
        captchaChecked: Boolean
    ): Result<PatientProfile> {
        if (fullName.isBlank() || emailOrPhone.isBlank() || birthPlaceDate.isBlank()) {
            return Result.failure(IllegalArgumentException("Semua kolom data diri wajib dilengkapi"))
        }
        if (nik.length != 16 || !nik.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("NIK harus terdiri dari 16 digit angka"))
        }
        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("Kata sandi minimal 8 karakter"))
        }
        if (!captchaChecked) {
            return Result.failure(IllegalArgumentException("Harap centang verifikasi CAPTCHA"))
        }
        // Realistic Lifecycle Rule #1: Fresh account starts with activeQueue = null
        queueRepository.clearForFreshAccount()
        return authRepository.register(
            fullName = fullName.trim(),
            emailOrPhone = emailOrPhone.trim(),
            nik = nik,
            birthPlaceDate = birthPlaceDate.trim(),
            password = password
        )
    }
}

class ObserveActiveQueueUseCase @Inject constructor(
    private val queueRepository: QueueRepository
) {
    operator fun invoke(): StateFlow<ActiveQueue?> = queueRepository.activeQueueFlow
}
