package com.ruangtunggu.ui.screens.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruangtunggu.domain.model.ActiveQueue
import com.ruangtunggu.domain.model.CompletedConsultation
import com.ruangtunggu.domain.model.HospitalItem
import com.ruangtunggu.domain.model.PatientProfile
import com.ruangtunggu.domain.model.QueueHistoryItem
import com.ruangtunggu.domain.model.QueuePhase
import com.ruangtunggu.domain.repository.AuthRepository
import com.ruangtunggu.domain.repository.QueueRepository
import com.ruangtunggu.ui.components.HomeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BerandaUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTab: HomeTab = HomeTab.BERANDA,
    val patientProfile: PatientProfile = PatientProfile(),
    val greetingTitle: String = "Selamat pagi 👋",
    val unreadNotificationCount: Int = 1,
    val searchQuery: String = "",
    val activeQueue: ActiveQueue? = null,
    val completedConsultation: CompletedConsultation? = null,
    val popularPoliList: List<String> = listOf("Umum", "Anak", "Gigi", "Kandungan"),
    val selectedPoliChip: String = "Umum",
    val nearbyHospitals: List<HospitalItem> = emptyList(),
    val queueHistory: List<QueueHistoryItem> = emptyList(),
    val selectedHistoryFilter: Int = 0,
    val showCancelBottomSheet: Boolean = false
)

@HiltViewModel
class BerandaViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BerandaUiState())
    val uiState: StateFlow<BerandaUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
        observeReactiveFlows()
    }

    fun setInitialTab(tabName: String) {
        val tab = runCatching { HomeTab.valueOf(tabName) }.getOrDefault(HomeTab.BERANDA)
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(280)
            val poli = queueRepository.getPopularPoli()
            val hospitals = queueRepository.getNearbyHospitals()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    popularPoliList = poli,
                    nearbyHospitals = hospitals
                )
            }
        }
    }

    private fun observeReactiveFlows() {
        viewModelScope.launch {
            authRepository.currentProfileFlow.collect { profile ->
                _uiState.update { it.copy(patientProfile = profile) }
            }
        }
        viewModelScope.launch {
            queueRepository.activeQueueFlow.collect { queue ->
                _uiState.update { it.copy(activeQueue = queue) }
            }
        }
        viewModelScope.launch {
            queueRepository.completedConsultationFlow.collect { completed ->
                _uiState.update { it.copy(completedConsultation = completed) }
            }
        }
        viewModelScope.launch {
            queueRepository.historyFlow.collect { history ->
                _uiState.update { it.copy(queueHistory = history) }
            }
        }
        viewModelScope.launch {
            queueRepository.notificationsFlow.collect { notifs ->
                val unread = notifs.count { it.isUnread }
                _uiState.update { it.copy(unreadNotificationCount = unread) }
            }
        }
    }

    fun onTabSelected(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            val filtered = queueRepository.getNearbyHospitals(
                query = query,
                poliFilter = "Semua"
            )
            _uiState.update { it.copy(nearbyHospitals = filtered) }
        }
    }

    fun onPoliChipSelected(poli: String) {
        _uiState.update { it.copy(selectedPoliChip = poli) }
    }

    fun onSimulateNextQueueStep() {
        viewModelScope.launch {
            queueRepository.advanceQueueSimulation()
        }
    }

    fun onSetQueuePhase(phase: QueuePhase) {
        viewModelScope.launch {
            queueRepository.setQueuePhase(phase)
        }
    }

    fun onCompleteQueueForReview() {
        viewModelScope.launch {
            queueRepository.completeActiveQueueForReview()
        }
    }

    fun onDismissReviewCard() {
        viewModelScope.launch {
            queueRepository.dismissConsultationReview()
        }
    }

    fun onOpenCancelSheet() {
        _uiState.update { it.copy(showCancelBottomSheet = true) }
    }

    fun onDismissCancelSheet() {
        _uiState.update { it.copy(showCancelBottomSheet = false) }
    }

    fun onConfirmCancelQueue(reason: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(showCancelBottomSheet = false) }
            queueRepository.cancelActiveQueue(reason)
        }
    }

    fun onHistoryFilterSelected(index: Int) {
        _uiState.update { it.copy(selectedHistoryFilter = index) }
    }

    fun onLogout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
