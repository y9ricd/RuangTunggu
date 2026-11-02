package com.ruangtunggu.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val nikOrEmail: String = "",
    val password: String = "",
    val captchaChecked: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false
) {
    val isIdentifierValid: Boolean
        get() = nikOrEmail.isNotBlank() && (
            nikOrEmail.contains("@") || (nikOrEmail.length == 16 && nikOrEmail.all { it.isDigit() })
        )
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onNikOrEmailChanged(value: String) {
        _uiState.update { it.copy(nikOrEmail = value, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onCaptchaChanged(checked: Boolean) {
        _uiState.update { it.copy(captchaChecked = checked, errorMessage = null) }
    }

    fun submitLogin(onSuccess: () -> Unit) {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(
                nikOrEmail = current.nikOrEmail,
                password = current.password,
                captchaChecked = current.captchaChecked
            )
            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(isLoading = false, isLoginSuccess = true, errorMessage = null)
                    }
                    onSuccess()
                },
                onFailure = { err ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = err.message ?: "Email/password salah"
                        )
                    }
                }
            )
        }
    }
}
