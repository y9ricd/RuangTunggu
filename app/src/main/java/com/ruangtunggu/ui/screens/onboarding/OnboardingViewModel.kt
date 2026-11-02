package com.ruangtunggu.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import com.ruangtunggu.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    fun completeOnboarding() {
        preferenceManager.isOnboardingCompleted = true
    }
}

