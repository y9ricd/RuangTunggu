package com.ruangtunggu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.data.local.PreferenceManager
import com.ruangtunggu.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val identifier: String = "pasien@ruangtunggu.com", // pre-filled demo
    val password: String = "password123", // pre-filled demo
    val isCaptchaVerified: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdentifierChanged(value: String) {
        _uiState.value = _uiState.value.copy(identifier = value, errorMessage = null)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onCaptchaToggled(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isCaptchaVerified = checked, errorMessage = null)
    }

    fun login() {
        val state = _uiState.value
        if (state.identifier.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Masukkan NIK atau Email Anda")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Masukkan kata sandi Anda")
            return
        }
        if (!state.isCaptchaVerified) {
            _uiState.value = state.copy(errorMessage = "Silakan centang verifikasi CAPTCHA terlebih dahulu")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.login(
                identifier = state.identifier,
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
                        errorMessage = error.message ?: "Login gagal. Coba lagi"
                    )
                }
            )
        }
    }

    fun onBiometricSuccess() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            preferenceManager.savedUserId = "user_demo_01"
            preferenceManager.savedUserName = "Nama Pasien"
            _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
        }
    }
}

