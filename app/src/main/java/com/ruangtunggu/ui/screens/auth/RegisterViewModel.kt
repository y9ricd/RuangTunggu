package com.ruangtunggu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val namaLengkap: String = "",
    val emailOrPhone: String = "",
    val nik: String = "",
    val tempatTanggalLahir: String = "",
    val password: String = "",
    val isCaptchaVerified: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNamaLengkapChanged(value: String) {
        _uiState.value = _uiState.value.copy(namaLengkap = value, errorMessage = null)
    }

    fun onEmailOrPhoneChanged(value: String) {
        _uiState.value = _uiState.value.copy(emailOrPhone = value, errorMessage = null)
    }

    fun onNikChanged(value: String) {
        if (value.length <= 16 && value.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(nik = value, errorMessage = null)
        }
    }

    fun onTempatTanggalLahirChanged(value: String) {
        _uiState.value = _uiState.value.copy(tempatTanggalLahir = value, errorMessage = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onCaptchaToggled(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isCaptchaVerified = checked, errorMessage = null)
    }

    fun register() {
        val state = _uiState.value

        if (state.namaLengkap.trim().length < 3) {
            _uiState.value = state.copy(errorMessage = "Nama lengkap harus diisi (sesuai KTP)")
            return
        }
        if (state.emailOrPhone.trim().isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email atau No. HP harus diisi")
            return
        }
        if (state.nik.length != 16) {
            _uiState.value = state.copy(errorMessage = "NIK harus terdiri dari 16 digit angka")
            return
        }
        if (state.tempatTanggalLahir.trim().isBlank()) {
            _uiState.value = state.copy(errorMessage = "Tempat dan tanggal lahir harus diisi")
            return
        }
        if (state.password.length < 8) {
            _uiState.value = state.copy(errorMessage = "Kata sandi minimal 8 karakter")
            return
        }
        if (!state.isCaptchaVerified) {
            _uiState.value = state.copy(errorMessage = "Silakan centang verifikasi CAPTCHA terlebih dahulu")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val email = if (state.emailOrPhone.contains("@")) state.emailOrPhone.trim() else "${state.nik}@ruangtunggu.com"
            val phone = if (!state.emailOrPhone.contains("@")) state.emailOrPhone.trim() else "081234567890"

            val result = authRepository.register(
                nama = state.namaLengkap,
                nik = state.nik,
                email = email,
                noHp = phone,
                password = state.password,
                captchaToken = "valid_captcha_token"
            )
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Registrasi gagal. Silakan coba lagi."
                    )
                }
            )
        }
    }
}
