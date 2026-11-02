package com.ruangtunggu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.data.repository.AuthRepository
import com.ruangtunggu.util.isValidEmail
import com.ruangtunggu.util.isValidNik
import com.ruangtunggu.util.isValidPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val nama: String = "",
    val nik: String = "",
    val email: String = "",
    val noHp: String = "",
    val password: String = "",
    val confirmPassword: String = "",
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

    fun onNamaChanged(value: String) {
        _uiState.value = _uiState.value.copy(nama = value, errorMessage = null)
    }

    fun onNikChanged(value: String) {
        if (value.length <= 16 && value.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(nik = value, errorMessage = null)
        }
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onNoHpChanged(value: String) {
        if (value.all { it.isDigit() || it == '+' }) {
            _uiState.value = _uiState.value.copy(noHp = value, errorMessage = null)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value, errorMessage = null)
    }

    fun onCaptchaToggled(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isCaptchaVerified = checked, errorMessage = null)
    }

    fun register() {
        val state = _uiState.value

        if (state.nama.trim().length < 3) {
            _uiState.value = state.copy(errorMessage = "Nama lengkap minimal 3 karakter")
            return
        }
        if (!state.nik.isValidNik()) {
            _uiState.value = state.copy(errorMessage = "NIK harus terdiri dari 16 digit angka")
            return
        }
        if (!state.email.isValidEmail()) {
            _uiState.value = state.copy(errorMessage = "Format alamat email tidak valid")
            return
        }
        if (!state.noHp.isValidPhone()) {
            _uiState.value = state.copy(errorMessage = "Format nomor HP tidak valid (min 10 digit)")
            return
        }
        if (state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Kata sandi minimal 6 karakter")
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Konfirmasi kata sandi tidak cocok")
            return
        }
        if (!state.isCaptchaVerified) {
            _uiState.value = state.copy(errorMessage = "Silakan centang verifikasi CAPTCHA")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.register(
                nama = state.nama,
                nik = state.nik,
                email = state.email,
                noHp = state.noHp,
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
                        errorMessage = error.message ?: "Registrasi gagal. Coba lagi"
                    )
                }
            )
        }
    }
}

