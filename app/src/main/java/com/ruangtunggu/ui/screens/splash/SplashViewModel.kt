package com.ruangtunggu.ui.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SplashUiState(
    val appTitle: String = "RuangTunggu",
    val appSubtitle: String = "Antre tanpa ribet, sehat tanpa menunggu",
    val heroHeading: String = "Antre dari mana saja",
    val heroDescription: String = "Daftar, pantau, dan dapatkan notifikasi\ngiliran Anda langsung dari ponsel.",
    val primaryButtonText: String = "Mulai",
    val secondaryLinkText: String = "Sudah punya akun? Masuk"
)

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()
}
