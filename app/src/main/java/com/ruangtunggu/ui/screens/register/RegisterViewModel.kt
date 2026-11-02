package com.ruangtunggu.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val emailOrPhone: String = "",
    val nik: String = "",
    val birthPlaceDate: String = "",
    val password: String = "",
    val captchaChecked: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onEmailOrPhoneChanged(value: String) {
        _uiState.update { it.copy(emailOrPhone = value, errorMessage = null) }
    }

    fun onNikChanged(value: String) {
        val numeric16 = value.filter { it.isDigit() }.take(16)
        _uiState.update { it.copy(nik = numeric16, errorMessage = null) }
    }

    fun onBirthPlaceDateChanged(value: String) {
        _uiState.update { it.copy(birthPlaceDate = value, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onCaptchaChanged(checked: Boolean) {
        _uiState.update { it.copy(captchaChecked = checked, errorMessage = null) }
    }

    fun submitRegistration(onSuccess: () -> Unit) {
        val s = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = registerUseCase(
                fullName = s.fullName,
                emailOrPhone = s.emailOrPhone,
                nik = s.nik,
                birthPlaceDate = s.birthPlaceDate,
                password = s.password,
                captchaChecked = s.captchaChecked
            )
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    onSuccess()
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = err.message ?: "Data pendaftaran belum sesuai"
                        )
                    }
                }
            )
        }
    }
}
