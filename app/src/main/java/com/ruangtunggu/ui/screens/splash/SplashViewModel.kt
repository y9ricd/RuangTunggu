package com.ruangtunggu.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.data.local.PreferenceManager
import com.ruangtunggu.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SplashDestination {
    LOADING,
    ONBOARDING,
    LOGIN,
    HOME
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _destination = MutableStateFlow(SplashDestination.LOADING)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        checkStartRoute()
    }

    private fun checkStartRoute() {
        viewModelScope.launch {
            delay(1500) // Branding splash delay
            if (!preferenceManager.isOnboardingCompleted) {
                _destination.value = SplashDestination.ONBOARDING
            } else if (authRepository.isLoggedIn()) {
                _destination.value = SplashDestination.HOME
            } else {
                _destination.value = SplashDestination.LOGIN
            }
        }
    }
}

