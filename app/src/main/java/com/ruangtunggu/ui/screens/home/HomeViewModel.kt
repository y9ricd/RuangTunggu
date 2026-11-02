package com.ruangtunggu.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.data.local.PreferenceManager
import com.ruangtunggu.data.model.Hospital
import com.ruangtunggu.data.model.Polyclinic
import com.ruangtunggu.data.model.Queue
import com.ruangtunggu.data.repository.HospitalRepository
import com.ruangtunggu.data.repository.QueueRepository
import com.ruangtunggu.util.greeting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val greetingText: String = "",
    val patientName: String = "Nama Pasien",
    val searchQuery: String = "",
    val activeQueue: Queue? = null,
    val popularPolis: List<Polyclinic> = emptyList(),
    val selectedPoliId: String = "poli_anak",
    val nearbyHospitals: List<Hospital> = emptyList(),
    val isLoading: Boolean = false,
    val unreadNotifCount: Int = 2
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val hospitalRepository: HospitalRepository,
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                greetingText = greeting(),
                patientName = preferenceManager.savedUserName ?: "Nama Pasien",
                isLoading = true
            )

            // Load active queue
            val userId = preferenceManager.savedUserId ?: "user_01"
            val queueResult = queueRepository.getActiveQueue(userId)
            val queue = queueResult.getOrNull()

            // Load popular polyclinics
            val polisResult = hospitalRepository.getPopularPolyclinics()
            val polis = polisResult.getOrDefault(emptyList())

            // Load nearby hospitals
            val hospitalsResult = hospitalRepository.getNearbyHospitals(-7.45, 112.71)
            val hospitals = hospitalsResult.getOrDefault(emptyList())

            _uiState.value = _uiState.value.copy(
                activeQueue = queue,
                popularPolis = polis,
                nearbyHospitals = hospitals,
                isLoading = false
            )
        }
    }

    fun onPoliSelected(poliId: String) {
        _uiState.value = _uiState.value.copy(selectedPoliId = poliId)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}

